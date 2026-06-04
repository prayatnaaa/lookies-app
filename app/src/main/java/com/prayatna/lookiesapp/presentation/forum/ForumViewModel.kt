package com.prayatna.lookiesapp.presentation.forum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.message.CreateForumMessageInput
import com.prayatna.lookiesapp.domain.usecase.chat.*
import com.prayatna.lookiesapp.domain.usecase.user.GetProfileUseCase
import com.prayatna.lookiesapp.presentation.forum.state.ForumEvent
import com.prayatna.lookiesapp.presentation.forum.state.ForumUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForumViewModel @Inject constructor(
    private val listenToForumMessagesUseCase: ListenToForumMessagesUseCase,
    private val insertForumsMessageUseCase: InsertForumsMessageUseCase,
    private val updateForumMessageUseCase: UpdateForumMessageUseCase,
    private val deleteForumMessageUseCase: DeleteForumMessageUseCase,
    private val pinForumMessageUseCase: PinForumMessageUseCase,
    private val getForumChannelsUseCase: GetForumChannelsUseCase,
    private val getForumsUseCase: GetForumsUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForumUiState())
    val uiState: StateFlow<ForumUiState> = _uiState.asStateFlow()

    private var listenJob: Job? = null

    init {
        observeCurrentUser()
    }

    fun onEvent(event: ForumEvent) {
        when (event) {
            is ForumEvent.InitChannel -> handleInitChannel(event.channelId)
            is ForumEvent.InputChanged -> updateInput(event.text)
            ForumEvent.SendMessage -> handleSendMessage()
            ForumEvent.ClearError -> clearError()
            
            is ForumEvent.StartEditing -> {
                _uiState.update { it.copy(editingMessage = event.message, currentInputString = event.message.content) }
            }
            ForumEvent.CancelEditing -> {
                _uiState.update { it.copy(editingMessage = null, currentInputString = "") }
            }
            is ForumEvent.UpdateMessage -> handleUpdateMessage(event.messageId, event.newContent)
            is ForumEvent.DeleteMessage -> handleDeleteMessage(event.messageId)
            is ForumEvent.TogglePinMessage -> handleTogglePin(event.messageId, event.currentPinned)
        }
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            getProfileUseCase().collect { result ->
                if (result is DataResult.Success) {
                    _uiState.update {
                        it.copy(currentUserId = result.data.id.orEmpty())
                    }
                }
            }
        }
    }

    private fun handleInitChannel(channelId: String) {
        val current = _uiState.value
        if (current.channelId == channelId && current.messages.isNotEmpty()) return

        _uiState.update {
            it.copy(
                channelId = channelId,
                isLoading = true,
                errorMessage = null
            )
        }

        // Fetch all forums to find the one that has this channel
        viewModelScope.launch {
            val forumsResult = getForumsUseCase()
            if (forumsResult is DataResult.Success) {
                // Iterate through forums to find the channel and the user's role
                for (forum in forumsResult.data) {
                    val channelsResult = getForumChannelsUseCase(forum.id)
                    if (channelsResult is DataResult.Success) {
                        val channel = channelsResult.data.find { it.id == channelId }
                        if (channel != null) {
                            _uiState.update { it.copy(userRole = channel.role) }
                            break
                        }
                    }
                }
            }
        }

        listenMessages(channelId)
    }

    private fun listenMessages(channelId: String) {
        listenJob?.cancel()
        listenJob = viewModelScope.launch {
            listenToForumMessagesUseCase(channelId)
                .distinctUntilChanged()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Unknown error"
                        )
                    }
                }
                .collect { messages ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            messages = messages.sortedBy { msg -> msg.createdAt }
                        )
                    }
                }
        }
    }

    private fun updateInput(text: String) {
        _uiState.update {
            it.copy(currentInputString = text)
        }
    }

    private fun handleSendMessage() {
        val current = _uiState.value
        if (current.editingMessage != null) {
            handleUpdateMessage(current.editingMessage.id, current.currentInputString)
            return
        }

        val channelId = current.channelId
        val content = current.currentInputString.trim()
        val senderId = current.currentUserId

        if (channelId.isEmpty() || content.isEmpty() || senderId.isEmpty()) return

        _uiState.update { it.copy(isSending = true, errorMessage = null) }

        viewModelScope.launch {
            val result = insertForumsMessageUseCase(
                CreateForumMessageInput(
                    channelId = channelId,
                    content = content,
                    senderId = senderId
                )
            )

            _uiState.update { state ->
                when (result) {
                    is DataResult.Success -> state.copy(isSending = false, currentInputString = "")
                    is DataResult.Error -> state.copy(isSending = false, errorMessage = result.error)
                    else -> state.copy(isSending = false)
                }
            }
        }
    }

    private fun handleUpdateMessage(id: String, content: String) {
        if (content.isBlank()) return
        _uiState.update { it.copy(isSending = true) }
        
        viewModelScope.launch {
            when (val result = updateForumMessageUseCase(id, content)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isSending = false, editingMessage = null, currentInputString = "") }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isSending = false, errorMessage = result.error) }
                }
                else -> _uiState.update { it.copy(isSending = false) }
            }
        }
    }

    private fun handleDeleteMessage(id: String) {
        viewModelScope.launch {
            when (val result = deleteForumMessageUseCase(id)) {
                is DataResult.Success -> { /* Realtime will handle update */ }
                is DataResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }

    private fun handleTogglePin(id: String, currentPinned: Boolean) {
        viewModelScope.launch {
            when (val result = pinForumMessageUseCase(id, !currentPinned)) {
                is DataResult.Success -> { /* Realtime will handle update */ }
                is DataResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    override fun onCleared() {
        listenJob?.cancel()
        super.onCleared()
    }
}

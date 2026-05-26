package com.prayatna.lookiesapp.presentation.forum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.message.CreateForumMessageInput
import com.prayatna.lookiesapp.domain.usecase.chat.InsertForumsMessageUseCase
import com.prayatna.lookiesapp.domain.usecase.chat.ListenToForumMessagesUseCase
import com.prayatna.lookiesapp.domain.usecase.user.GetProfileUseCase
import com.prayatna.lookiesapp.presentation.forum.state.ForumEvent
import com.prayatna.lookiesapp.presentation.forum.state.ForumUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForumViewModel @Inject constructor(
    private val listenToForumMessagesUseCase: ListenToForumMessagesUseCase,
    private val insertForumsMessageUseCase: InsertForumsMessageUseCase,
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

        val channelId = current.channelId
        val content = current.currentInputString.trim()
        val senderId = current.currentUserId

        if (channelId.isEmpty() || content.isEmpty() || senderId.isEmpty()) return

        _uiState.update {
            it.copy(isSending = true, errorMessage = null)
        }

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
                    is DataResult.Success -> {
                        state.copy(
                            isSending = false,
                            currentInputString = ""
                        )
                    }

                    is DataResult.Error -> {
                        state.copy(
                            isSending = false,
                            errorMessage = result.error
                        )
                    }

                    else -> state.copy(isSending = false)
                }
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
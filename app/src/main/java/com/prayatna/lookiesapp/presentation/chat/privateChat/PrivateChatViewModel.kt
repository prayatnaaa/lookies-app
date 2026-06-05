package com.prayatna.lookiesapp.presentation.chat.privateChat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.message.CreateMessageInput
import com.prayatna.lookiesapp.domain.usecase.chat.ListenToMessagesUseCase
import com.prayatna.lookiesapp.domain.usecase.chat.SendMessageUseCase
import com.prayatna.lookiesapp.domain.usecase.user.GetProfileUseCase
import com.prayatna.lookiesapp.presentation.chat.privateChat.state.PrivateChatEffect
import com.prayatna.lookiesapp.presentation.chat.privateChat.state.PrivateChatEvent
import com.prayatna.lookiesapp.presentation.chat.privateChat.state.PrivateChatUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivateChatViewModel @Inject constructor(
    private val listenToMessagesUseCase: ListenToMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrivateChatUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<PrivateChatEffect>()
    val effect = _effect.asSharedFlow()

    private var listenJob: Job? = null

    init {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            getProfileUseCase().collect { result ->
                if (result is DataResult.Success) {
                    _uiState.update { it.copy(currentUserId = result.data.id.orEmpty()) }
                }
            }
        }
    }

    fun onEvent(event: PrivateChatEvent) {
        when (event) {
            is PrivateChatEvent.InitChat -> {
                _uiState.update { it.copy(conversationId = event.conversationId, otherPartyName = event.otherPartyName) }
                startListening(event.conversationId)
            }
            is PrivateChatEvent.InputChanged -> {
                _uiState.update { it.copy(currentInputString = event.text) }
            }
            PrivateChatEvent.SendMessage -> handleSendMessage()
            PrivateChatEvent.OnBackClicked -> {
                viewModelScope.launch { _effect.emit(PrivateChatEffect.NavigateBack) }
            }
        }
    }

    private fun startListening(conversationId: String) {
        listenJob?.cancel()
        listenJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            listenToMessagesUseCase(conversationId)
                .catch { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
                .collect { messages ->
                    _uiState.update { it.copy(isLoading = false, messages = messages) }
                }
        }
    }

    private fun handleSendMessage() {
        val state = _uiState.value
        val content = state.currentInputString.trim()
        if (content.isEmpty()) return

        val input = CreateMessageInput(
            conversationId = state.conversationId,
            senderType = "user", // TODO: determine dynamically
            content = content
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true, currentInputString = "") }
            when (val result = sendMessageUseCase(input)) {
                is DataResult.Error -> {
                    _uiState.update { it.copy(isSending = false, errorMessage = result.error) }
                }
                is DataResult.Success -> {
                    _uiState.update { it.copy(isSending = false) }
                }
                else -> _uiState.update { it.copy(isSending = false) }
            }
        }
    }

    override fun onCleared() {
        listenJob?.cancel()
        super.onCleared()
    }
}

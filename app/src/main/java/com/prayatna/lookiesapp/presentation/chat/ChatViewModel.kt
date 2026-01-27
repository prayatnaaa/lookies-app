package com.prayatna.lookiesapp.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.data.remote.dto.request.chat.CreateMessageRequest
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.presentation.chat.state.ChatEvent
import com.prayatna.lookiesapp.presentation.chat.state.ChatUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userPreferences: UserPreference
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private var messageJob: Job? = null

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.UpdateInput -> {
                _uiState.update { it.copy(messageInput = event.value) }
            }
            is ChatEvent.SendMessage -> {
                sendMessage(event.receiverId)
            }
        }
    }

    fun loadMessages(targetId: String) {
        messageJob?.cancel()

        _uiState.update { it.copy(isLoading = true) }

        when (val result = chatRepository.getMessages(targetId = targetId)) {
            is DataResult.Success -> {
                messageJob = viewModelScope.launch {
                    val user = userPreferences.getProfile().first()
                    result.data.collect { listMessages ->
                        val sortedMessages = listMessages.sortedBy { it.sentAt }
                        _uiState.update {
                            it.copy(
                                currentUserId = user.id ?: "",
                                messages = sortedMessages,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                }
            }
            is DataResult.Error -> {
                _uiState.update {
                    it.copy(isLoading = false, error = result.error)
                }
            }
            is DataResult.Loading -> {
                _uiState.update { it.copy(isLoading = true) }
            }

            else -> {}
        }
    }

    private fun sendMessage(receiverId: String) {
        val content = uiState.value.messageInput.trim()
        if (content.isBlank()) return

        viewModelScope.launch {
            val senderId = userPreferences.getProfile().first().id.orEmpty()

            val request = CreateMessageRequest(
                senderId = senderId,
                receiverId = receiverId,
                content = content
            )

            _uiState.update { it.copy(messageInput = "") }

            when (val result = chatRepository.createMessage(request)) {
                is DataResult.Success -> {

                }

                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(error = result.error)
                    }
                }

                is DataResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                else -> Unit
            }
        }
    }
}
package com.prayatna.lookiesapp.presentation.forum

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.message.CreateForumMessageInput
import com.prayatna.lookiesapp.domain.usecase.chat.InsertForumsMessageUseCase
import com.prayatna.lookiesapp.domain.usecase.chat.ListenToForumMessagesUseCase
import com.prayatna.lookiesapp.domain.usecase.user.GetProfileUseCase
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

    fun initChannel(channelId: String) {
        if (_uiState.value.channelId == channelId && _uiState.value.messages.isNotEmpty()) return

        _uiState.update {
            it.copy(
                channelId = channelId,
                isLoading = true,
                errorMessage = null
            )
        }

        startListeningToMessages(channelId)
    }

    private fun startListeningToMessages(channelId: String) {
        listenJob?.cancel()

        listenJob = viewModelScope.launch {
            listenToForumMessagesUseCase(channelId).distinctUntilChanged()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message
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

    fun onInputTextChanged(text: String) {
        _uiState.update { it.copy(currentInputString = text) }
    }

    fun sendMessage() {
        val channelId = _uiState.value.channelId
        val content = _uiState.value.currentInputString.trim()
        
        if (channelId.isEmpty() || content.isEmpty()) return

        _uiState.update { it.copy(isSending = true, errorMessage = null) }

        viewModelScope.launch {
            val input = CreateForumMessageInput(
                channelId = channelId,
                content = content,
                senderId = "1"
            )
            
            val result = insertForumsMessageUseCase(input)
            
            _uiState.update { currentState ->
                when (result) {
                    is DataResult.Success -> {
                        currentState.copy(isSending = false, currentInputString = "")
                    }
                    is DataResult.Error -> {
                        currentState.copy(isSending = false, errorMessage = result.error)
                    }
                    else -> currentState.copy(isSending = false)
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

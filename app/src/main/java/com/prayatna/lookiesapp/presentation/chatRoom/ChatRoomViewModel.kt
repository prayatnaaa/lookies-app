package com.prayatna.lookiesapp.presentation.chatRoom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.presentation.chatRoom.state.ChatRoomUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatRoomUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadInbox()
    }

    fun loadInbox() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = chatRepository.getChatInbox()) {
                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = result.error, isLoading = false)
                    }
                }
                is DataResult.Success -> {
                    result.data.collect { inboxList ->
                        _uiState.update {
                            it.copy(chatRooms = inboxList, isLoading = false)
                        }
                    }
                }
                else -> {}
            }
        }
    }
}
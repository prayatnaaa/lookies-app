package com.prayatna.lookiesapp.presentation.chat.conversationList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.chat.GetConversationsUseCase
import com.prayatna.lookiesapp.presentation.chat.conversationList.state.ConversationListEffect
import com.prayatna.lookiesapp.presentation.chat.conversationList.state.ConversationListEvent
import com.prayatna.lookiesapp.presentation.chat.conversationList.state.ConversationListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationListViewModel @Inject constructor(
    private val getConversationsUseCase: GetConversationsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConversationListUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ConversationListEffect>()
    val effect = _effect.asSharedFlow()

//    init {
//        loadData()
//        getRole()
//    }

    fun onEvent(event: ConversationListEvent) {
        when (event) {
            ConversationListEvent.LoadConversations -> loadData()
            is ConversationListEvent.OnConversationClicked -> {
                viewModelScope.launch {
                    _effect.emit(ConversationListEffect.NavigateToChat(event.conversationId, event.otherPartyName))
                }
            }
            ConversationListEvent.OnBackClicked -> {
                viewModelScope.launch {
                    _effect.emit(ConversationListEffect.NavigateBack)
                }
            }
        }
    }

    fun loadData() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when( val result = getConversationsUseCase()) {
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                is DataResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, conversations = result.data) }
                }
                else -> Unit
            }
        }
    }
}

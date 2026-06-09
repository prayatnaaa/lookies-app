package com.prayatna.lookiesapp.presentation.chat.privateChat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.message.CreateMessageInput
import com.prayatna.lookiesapp.domain.model.message.MessageMetadata
import com.prayatna.lookiesapp.domain.usecase.chat.GetConversationByMerchantIdUseCase
import com.prayatna.lookiesapp.domain.usecase.chat.GetOrCreateConversationUseCase
import com.prayatna.lookiesapp.domain.usecase.chat.ListenToMessagesUseCase
import com.prayatna.lookiesapp.domain.usecase.chat.MarkMessagesAsReadUseCase
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
    private val getProfileUseCase: GetProfileUseCase,
    private val getOrCreateConversationUseCase: GetOrCreateConversationUseCase,
    private val getConversationByMerchantIdUseCase: GetConversationByMerchantIdUseCase,
    private val markMessagesAsReadUseCase: MarkMessagesAsReadUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val isMerchant = savedStateHandle.get<Boolean>("isMerchant") ?: false
    private var merchantId: String? = null
    
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
                this.merchantId = event.merchantId
                
                val metadata = if (event.metadataType != null && event.metadataId != null && event.metadataImageUrl != null && event.metadataTitle != null) {
                    MessageMetadata(
                        type = event.metadataType,
                        id = event.metadataId,
                        imageUrl = event.metadataImageUrl,
                        title = event.metadataTitle
                    )
                } else null

                if (event.conversationId != null) {
                    _uiState.update { it.copy(
                        conversationId = event.conversationId, 
                        otherPartyName = event.otherPartyName,
                        pendingMetadata = metadata
                    ) }
                    startListening(event.conversationId)
                } else if (event.merchantId != null) {
                    _uiState.update { it.copy(
                        otherPartyName = event.otherPartyName,
                        pendingMetadata = metadata
                    ) }
                    checkExistingConversation(event.merchantId)
                }
            }
            is PrivateChatEvent.InputChanged -> {
                _uiState.update { it.copy(currentInputString = event.text) }
            }
            PrivateChatEvent.SendMessage -> handleSendMessage()
            PrivateChatEvent.OnBackClicked -> {
                viewModelScope.launch { _effect.emit(PrivateChatEffect.NavigateBack) }
            }

            PrivateChatEvent.OnClearMetadata -> {
                _uiState.update { it.copy(pendingMetadata = null) }
            }
        }
    }

    private fun checkExistingConversation(merchantId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getConversationByMerchantIdUseCase(merchantId)) {
                is DataResult.Success -> {
                    val conversationId = result.data?.conversationId
                    if (conversationId != null) {
                        _uiState.update { it.copy(conversationId = conversationId, isLoading = false) }
                        startListening(conversationId)
                    } else {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
                else -> {}
            }
        }
    }

    private fun startListening(conversationId: String) {
        if (conversationId.isBlank()) return
        
        listenJob?.cancel()
        listenJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            listenToMessagesUseCase(conversationId)
                .catch { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
                .collect { messages ->
                    _uiState.update { it.copy(isLoading = false, messages = messages) }
                    markAsRead(conversationId)
                }
        }
    }

    private fun markAsRead(conversationId: String) {
        viewModelScope.launch {
            markMessagesAsReadUseCase(conversationId, isMerchant)
        }
    }

    private fun handleSendMessage() {
        val state = _uiState.value
        val content = state.currentInputString.trim()
        if (content.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true, currentInputString = "") }
            
            var currentConvId = state.conversationId
            
            if (currentConvId.isBlank()) {
                val mid = merchantId
                if (mid == null) {
                    _uiState.update { it.copy(isSending = false, errorMessage = "Merchant information missing") }
                    return@launch
                }
                
                when (val convResult = getOrCreateConversationUseCase(mid)) {
                    is DataResult.Success -> {
                        currentConvId = convResult.data.conversationId
                        _uiState.update { it.copy(conversationId = currentConvId) }
                        startListening(currentConvId)
                    }
                    is DataResult.Error -> {
                        _uiState.update { it.copy(isSending = false, errorMessage = convResult.error) }
                        return@launch
                    }
                    else -> return@launch
                }
            }

            val input = CreateMessageInput(
                conversationId = currentConvId,
                senderType = if (isMerchant) "merchant" else "user",
                content = content,
                metadata = state.pendingMetadata
            )

            when (val result = sendMessageUseCase(input)) {
                is DataResult.Error -> {
                    _uiState.update { it.copy(isSending = false, errorMessage = result.error) }
                }
                is DataResult.Success -> {
                    _uiState.update { it.copy(isSending = false, pendingMetadata = null) }
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

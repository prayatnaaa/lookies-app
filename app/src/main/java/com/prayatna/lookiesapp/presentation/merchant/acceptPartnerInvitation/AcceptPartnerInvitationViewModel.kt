package com.prayatna.lookiesapp.presentation.merchant.acceptPartnerInvitation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.merchant.GetMerchantMembersUseCase
import com.prayatna.lookiesapp.domain.usecase.user.AcceptPartnerInvitationUseCase
import com.prayatna.lookiesapp.domain.usecase.user.RejectPartnerInvitationUseCase
import com.prayatna.lookiesapp.presentation.merchant.acceptPartnerInvitation.state.AcceptPartnerInvitationEffect
import com.prayatna.lookiesapp.presentation.merchant.acceptPartnerInvitation.state.AcceptPartnerInvitationEvent
import com.prayatna.lookiesapp.presentation.merchant.acceptPartnerInvitation.state.AcceptPartnerInvitationUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AcceptPartnerInvitationViewModel @Inject constructor(
    private val acceptPartnerInvitationUseCase: AcceptPartnerInvitationUseCase,
    private val rejectPartnerInvitationUseCase: RejectPartnerInvitationUseCase,
    private val getMerchantMembersUseCase: GetMerchantMembersUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AcceptPartnerInvitationUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<AcceptPartnerInvitationEffect>()
    val effect = _effect.receiveAsFlow()

    // merchantAccountId is passed as a navigation route argument
    private val merchantAccountId: String = savedStateHandle["merchantAccountId"] ?: ""

    init {
        onEvent(AcceptPartnerInvitationEvent.LoadInvitation)
    }

    fun onEvent(event: AcceptPartnerInvitationEvent) {
        when (event) {
            AcceptPartnerInvitationEvent.LoadInvitation -> loadInvitation()

            AcceptPartnerInvitationEvent.AcceptClicked -> acceptInvitation()

            AcceptPartnerInvitationEvent.RejectClicked -> rejectInvitation()

            AcceptPartnerInvitationEvent.BackClicked -> {
                viewModelScope.launch {
                    _effect.send(AcceptPartnerInvitationEffect.NavigateBack)
                }
            }

            AcceptPartnerInvitationEvent.DismissDialog -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    private fun loadInvitation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getMerchantMembersUseCase(null)) {
                is DataResult.Success -> {
                    // Find the invitation matching this merchantAccountId with "invited" status
                    val invitation = result.data.find {
                        it.merchantAccountId == merchantAccountId && it.status == "invited"
                    }
                    if (invitation != null) {
                        _uiState.update {
                            it.copy(isLoading = false, invitation = invitation)
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Invitation not found or already resolved"
                            )
                        }
                    }
                }
                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.error)
                    }
                }
                else -> Unit
            }
        }
    }

    private fun acceptInvitation() {
        if (merchantAccountId.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isAccepting = true) }
            when (val result = acceptPartnerInvitationUseCase(merchantAccountId)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isAccepting = false) }
                    _effect.send(AcceptPartnerInvitationEffect.ShowMessage(
                        "You've joined ${_uiState.value.invitation?.tradingName ?: "the merchant"} successfully!"
                    ))
                    _effect.send(AcceptPartnerInvitationEffect.InvitationAccepted)
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isAccepting = false) }
                    _effect.send(AcceptPartnerInvitationEffect.ShowMessage(result.error))
                }
                else -> Unit
            }
        }
    }

    private fun rejectInvitation() {
        if (merchantAccountId.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isRejecting = true) }
            when (val result = rejectPartnerInvitationUseCase(merchantAccountId)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isRejecting = false) }
                    _effect.send(AcceptPartnerInvitationEffect.ShowMessage("Invitation declined."))
                    _effect.send(AcceptPartnerInvitationEffect.InvitationRejected)
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isRejecting = false) }
                    _effect.send(AcceptPartnerInvitationEffect.ShowMessage(result.error))
                }
                else -> Unit
            }
        }
    }
}

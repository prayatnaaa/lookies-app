package com.prayatna.lookiesapp.presentation.merchant.inviteMerchantMember

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.merchant.InviteMerchantMemberInput
import com.prayatna.lookiesapp.domain.usecase.merchant.InviteMerchantMemberUseCase
import com.prayatna.lookiesapp.domain.usecase.user.GetUsersEmailUseCase
import com.prayatna.lookiesapp.presentation.merchant.inviteMerchantMember.state.InviteMerchantMemberEffect
import com.prayatna.lookiesapp.presentation.merchant.inviteMerchantMember.state.InviteMerchantMemberEvent
import com.prayatna.lookiesapp.presentation.merchant.inviteMerchantMember.state.InviteMerchantMemberUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class InviteMerchantMemberViewModel @Inject constructor(
    private val inviteMerchantMemberUseCase: InviteMerchantMemberUseCase,
    private val getUsersEmailUseCase: GetUsersEmailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(InviteMerchantMemberUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<InviteMerchantMemberEffect>()
    val effect = _effect.receiveAsFlow()

    private val businessId: String = savedStateHandle["businessId"] ?: ""
    private val _searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.length >= 3) {
                        loadEmails(query)
                    } else if (query.isEmpty()) {
                        _uiState.update { it.copy(filteredEmails = emptyList()) }
                    }
                }
        }
    }

    fun onEvent(event: InviteMerchantMemberEvent) {
        when (event) {
            InviteMerchantMemberEvent.LoadEmails -> loadEmails()
            is InviteMerchantMemberEvent.EmailChanged -> {
                _uiState.update { it.copy(selectedEmail = event.email) }
                _searchQuery.value = event.email
            }
            is InviteMerchantMemberEvent.RoleChanged -> {
                _uiState.update { it.copy(selectedRole = event.role) }
            }
            InviteMerchantMemberEvent.InviteClicked -> inviteMember()
            InviteMerchantMemberEvent.BackClicked -> {
                viewModelScope.launch { _effect.send(InviteMerchantMemberEffect.NavigateBack) }
            }
            InviteMerchantMemberEvent.DismissSuccess -> {
                _uiState.update { it.copy(inviteSuccess = false) }
            }
        }
    }

    private fun loadEmails(query: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getUsersEmailUseCase(query)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, emails = result.data, filteredEmails = result.data) }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }

    private fun inviteMember() {
        val selectedUser = _uiState.value.filteredEmails.find { it.email == _uiState.value.selectedEmail }
        if (selectedUser == null) {
            viewModelScope.launch { _effect.send(InviteMerchantMemberEffect.ShowMessage("Please select a registered email")) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isInviting = true) }
            val input = InviteMerchantMemberInput(
                merchantAccountId = businessId,
                userId = selectedUser.id,
                role = _uiState.value.selectedRole
            )
            when (val result = inviteMerchantMemberUseCase(input)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isInviting = false, inviteSuccess = true) }
                    _effect.send(InviteMerchantMemberEffect.ShowMessage("Member invited successfully"))
                }
                is DataResult.Error -> {
                    Log.e("InviteMember", businessId)
                    _uiState.update { it.copy(isInviting = false, errorMessage = result.error) }
                    _effect.send(InviteMerchantMemberEffect.ShowMessage(result.error))
                }
                else -> Unit
            }
        }
    }
}

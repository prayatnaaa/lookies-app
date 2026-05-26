package com.prayatna.lookiesapp.presentation.merchant.inviteMerchantMember.state

import com.prayatna.lookiesapp.domain.model.user.UserEmail

data class InviteMerchantMemberUiState(
    val isLoading: Boolean = false,
    val isInviting: Boolean = false,
    val emails: List<UserEmail> = emptyList(),
    val filteredEmails: List<UserEmail> = emptyList(),
    val selectedEmail: String = "",
    val selectedRole: String = "member",
    val errorMessage: String? = null,
    val inviteSuccess: Boolean = false
)

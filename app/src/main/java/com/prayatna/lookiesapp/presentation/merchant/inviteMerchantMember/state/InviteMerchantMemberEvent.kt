package com.prayatna.lookiesapp.presentation.merchant.inviteMerchantMember.state

sealed interface InviteMerchantMemberEvent {
    data object LoadEmails : InviteMerchantMemberEvent
    data class EmailChanged(val email: String) : InviteMerchantMemberEvent
    data class RoleChanged(val role: String) : InviteMerchantMemberEvent
    data object InviteClicked : InviteMerchantMemberEvent
    data object BackClicked : InviteMerchantMemberEvent
    data object DismissSuccess : InviteMerchantMemberEvent
}

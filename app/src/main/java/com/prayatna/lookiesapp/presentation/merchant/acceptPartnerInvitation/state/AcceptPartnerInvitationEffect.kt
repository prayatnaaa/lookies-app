package com.prayatna.lookiesapp.presentation.merchant.acceptPartnerInvitation.state

sealed interface AcceptPartnerInvitationEffect {
    data object NavigateBack : AcceptPartnerInvitationEffect
    data class ShowMessage(val message: String) : AcceptPartnerInvitationEffect
    data object InvitationAccepted : AcceptPartnerInvitationEffect
    data object InvitationRejected : AcceptPartnerInvitationEffect
}

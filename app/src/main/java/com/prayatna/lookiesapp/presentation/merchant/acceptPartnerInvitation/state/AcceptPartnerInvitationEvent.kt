package com.prayatna.lookiesapp.presentation.merchant.acceptPartnerInvitation.state

sealed interface AcceptPartnerInvitationEvent {
    data object LoadInvitation : AcceptPartnerInvitationEvent
    data object AcceptClicked : AcceptPartnerInvitationEvent
    data object RejectClicked : AcceptPartnerInvitationEvent
    data object BackClicked : AcceptPartnerInvitationEvent
    data object DismissDialog : AcceptPartnerInvitationEvent
}

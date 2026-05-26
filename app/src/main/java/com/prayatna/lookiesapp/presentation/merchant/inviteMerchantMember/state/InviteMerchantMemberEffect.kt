package com.prayatna.lookiesapp.presentation.merchant.inviteMerchantMember.state

sealed interface InviteMerchantMemberEffect {
    data object NavigateBack : InviteMerchantMemberEffect
    data class ShowMessage(val message: String) : InviteMerchantMemberEffect
}

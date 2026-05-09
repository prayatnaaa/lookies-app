package com.prayatna.lookiesapp.presentation.merchant.merchantMemberByMerchantId.state

sealed interface MerchantMemberByMerchantIdEvent {
    data object BackClicked : MerchantMemberByMerchantIdEvent
    data object Retry : MerchantMemberByMerchantIdEvent
    data object InviteMemberClicked : MerchantMemberByMerchantIdEvent
}

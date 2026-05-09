package com.prayatna.lookiesapp.presentation.partner.merchantMemberByMerchantId.state

sealed interface MerchantMemberByMerchantIdEvent {
    data object BackClicked : MerchantMemberByMerchantIdEvent
    data object Retry : MerchantMemberByMerchantIdEvent
}

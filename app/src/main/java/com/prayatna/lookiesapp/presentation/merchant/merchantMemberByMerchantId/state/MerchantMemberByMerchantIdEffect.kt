package com.prayatna.lookiesapp.presentation.merchant.merchantMemberByMerchantId.state

sealed interface MerchantMemberByMerchantIdEffect {
    data object NavigateBack : MerchantMemberByMerchantIdEffect
    data class NavigateInviteMember(val businessId: String) : MerchantMemberByMerchantIdEffect
}

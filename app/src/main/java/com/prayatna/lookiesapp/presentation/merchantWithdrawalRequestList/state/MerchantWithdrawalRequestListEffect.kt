package com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList.state

sealed interface MerchantWithdrawalRequestListEffect {
    data object NavigateBack : MerchantWithdrawalRequestListEffect
    data class NavigateToDetail(val id: String) : MerchantWithdrawalRequestListEffect
}
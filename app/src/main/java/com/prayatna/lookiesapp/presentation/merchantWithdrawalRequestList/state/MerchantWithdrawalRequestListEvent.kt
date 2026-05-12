package com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList.state

sealed interface MerchantWithdrawalRequestListEvent {
    data object BackClicked: MerchantWithdrawalRequestListEvent
    data class DetailClicked(val id: String): MerchantWithdrawalRequestListEvent
}
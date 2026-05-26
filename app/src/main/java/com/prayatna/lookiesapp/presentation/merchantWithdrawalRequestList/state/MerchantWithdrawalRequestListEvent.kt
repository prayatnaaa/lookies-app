package com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList.state

sealed interface MerchantWithdrawalRequestListEvent {
    data object CreateWithdrawalClicked : MerchantWithdrawalRequestListEvent
    data object BackClicked: MerchantWithdrawalRequestListEvent
    data class DetailClicked(val id: String): MerchantWithdrawalRequestListEvent
    data object Refresh : MerchantWithdrawalRequestListEvent
    data object ShowCreatedSnackBar: MerchantWithdrawalRequestListEvent
}
package com.prayatna.lookiesapp.presentation.admin.withdrawal.detail.state

sealed interface AdminWithdrawalDetailUiEffect {
    data class ShowToast(val message: String) : AdminWithdrawalDetailUiEffect
    data object NavigateBack : AdminWithdrawalDetailUiEffect
}

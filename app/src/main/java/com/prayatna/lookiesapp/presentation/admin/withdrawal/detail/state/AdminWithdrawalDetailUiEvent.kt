package com.prayatna.lookiesapp.presentation.admin.withdrawal.detail.state

sealed interface AdminWithdrawalDetailUiEvent {
    data class LoadDetail(val id: String) : AdminWithdrawalDetailUiEvent
    data class UpdateStatus(val id: String, val status: String, val notes: String? = null) : AdminWithdrawalDetailUiEvent
    data object ClearError : AdminWithdrawalDetailUiEvent
    data class ProcessPayout(val id: String): AdminWithdrawalDetailUiEvent
    data class OnNotesChanged(val value: String): AdminWithdrawalDetailUiEvent
}

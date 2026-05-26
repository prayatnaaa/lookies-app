package com.prayatna.lookiesapp.presentation.admin.withdrawal.list.state

sealed interface AdminWithdrawalListUiEvent {
    data object LoadWithdrawalRequests : AdminWithdrawalListUiEvent
    data object ClearError : AdminWithdrawalListUiEvent
}

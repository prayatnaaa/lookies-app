package com.prayatna.lookiesapp.presentation.monthlyFinancleList.state

sealed class MonthlyFinanceEvent {
    data object LoadData : MonthlyFinanceEvent()
    data class TabSelected(val index: Int) : MonthlyFinanceEvent()
    data object NavigateBack : MonthlyFinanceEvent()
    data object WithdrawalListClicked : MonthlyFinanceEvent()
    data class PayoutLogClicked(val orderId: String) : MonthlyFinanceEvent()
}

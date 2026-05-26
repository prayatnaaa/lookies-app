package com.prayatna.lookiesapp.presentation.monthlyFinancleList.state

sealed class MonthlyFinanceEffect {
    data class ShowToast(val message: String, val title: String) : MonthlyFinanceEffect()
    data object NavigateBack : MonthlyFinanceEffect()
    data class NavigateToWithdrawalList(val businessId: String) : MonthlyFinanceEffect()
}
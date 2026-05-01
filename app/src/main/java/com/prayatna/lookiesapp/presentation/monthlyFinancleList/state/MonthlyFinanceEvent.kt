package com.prayatna.lookiesapp.presentation.monthlyFinancleList.state

import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReportFilterInput

sealed class MonthlyFinanceEvent {
    data class GetMonthlyFinancialReport(val filter: MonthlyFinancialReportFilterInput) : MonthlyFinanceEvent()
    data object NavigateBack : MonthlyFinanceEvent()

    data class StartDateSelected(val date: String) : MonthlyFinanceEvent()
    data class EndDateSelected(val date: String) : MonthlyFinanceEvent()
    data class ItemTypeSelected(val type: String) : MonthlyFinanceEvent()
    data class EventIdSelected(val id: Int) : MonthlyFinanceEvent()
}
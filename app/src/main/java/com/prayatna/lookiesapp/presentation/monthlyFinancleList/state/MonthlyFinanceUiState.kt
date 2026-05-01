package com.prayatna.lookiesapp.presentation.monthlyFinancleList.state

import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReport

data class MonthlyFinanceUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val filterStartDate: String? = null,
    val filterEndDate: String? = null,
    val filterItemType: String? = null,
    val filterEventId: Int? = null,
    val monthlyFinancialReports: List<MonthlyFinancialReport> = emptyList()
)
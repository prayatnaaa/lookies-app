package com.prayatna.lookiesapp.domain.model.transaction

data class MonthlyFinancialReportFilterInput(
    val merchantAccountId: String,
    val startDate: String? = null,
    val endDate: String? = null,
    val itemType: String? = null,
    val eventId: Int? = null
)
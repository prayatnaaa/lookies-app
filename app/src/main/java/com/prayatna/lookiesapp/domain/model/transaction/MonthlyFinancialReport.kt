package com.prayatna.lookiesapp.domain.model.transaction

data class MonthlyFinancialReport(
    val reportMonth: String,
    val totalOrders: Long,
    val grossRevenue: Double,
    val platformFees: Double,
    val paymentGatewayFees: Double,
    val netRevenue: Double
)
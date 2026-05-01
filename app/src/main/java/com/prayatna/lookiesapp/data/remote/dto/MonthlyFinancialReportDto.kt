package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonthlyFinancialReportDto(
    @SerialName("report_month")
    val reportMonth: String,
    @SerialName("total_orders")
    val totalOrders: Long,
    @SerialName("gross_revenue")
    val grossRevenue: Double,
    @SerialName("platform_fees")
    val platformFees: Double,
    @SerialName("payment_gateway_fees")
    val paymentGatewayFees: Double,
    @SerialName("net_revenue")
    val netRevenue: Double
)
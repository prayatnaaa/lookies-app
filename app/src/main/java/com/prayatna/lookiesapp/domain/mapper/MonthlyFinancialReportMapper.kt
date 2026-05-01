package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.MonthlyFinancialReportDto
import com.prayatna.lookiesapp.data.remote.dto.request.transaction.MonthlyFinancialReportFilterRequest
import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReport
import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReportFilterInput

fun MonthlyFinancialReportDto.toDomain(): MonthlyFinancialReport {
    return MonthlyFinancialReport(
        reportMonth = reportMonth,
        totalOrders = totalOrders,
        grossRevenue = grossRevenue,
        platformFees = platformFees,
        paymentGatewayFees = paymentGatewayFees,
        netRevenue = netRevenue
    )
}

fun MonthlyFinancialReportFilterInput.toDto(): MonthlyFinancialReportFilterRequest {
    return MonthlyFinancialReportFilterRequest(
        merchantAccountId = merchantAccountId,
        startDate = startDate,
        endDate = endDate,
        itemType = itemType,
        eventId = eventId
    )
}
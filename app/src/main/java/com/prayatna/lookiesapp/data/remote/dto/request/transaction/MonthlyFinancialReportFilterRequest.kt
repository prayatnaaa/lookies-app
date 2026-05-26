package com.prayatna.lookiesapp.data.remote.dto.request.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonthlyFinancialReportFilterRequest(
    @SerialName("p_merchant_account_id")
    val merchantAccountId: String,
    @SerialName("p_start_date")
    val startDate: String? = null,
    @SerialName("p_end_date")
    val endDate: String? = null,
    @SerialName("p_item_type")
    val itemType: String? = null,
    @SerialName("p_event_id")
    val eventId: Int? = null
)
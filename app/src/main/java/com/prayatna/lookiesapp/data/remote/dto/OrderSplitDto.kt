package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class OrderSplitDto(

    @SerialName("id")
    val id: String,

    @SerialName("order_id")
    val orderId: String,

    @SerialName("merchant_id")
    val merchantId: String,

    @SerialName("gross_amount")
    val grossAmount: Double,

    @SerialName("platform_fee")
    val platformFee: Double = 0.0,

    @SerialName("net_amount")
    val netAmount: Double,

    @SerialName("payout_status")
    val payoutStatus: String = "pending",

    @SerialName("payout_reference")
    val payoutReference: String? = null,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("payout_response_payload")
    val payoutResponsePayload: JsonObject? = null
)
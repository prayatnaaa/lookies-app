package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class RefundDto(
    val id: String,

    @SerialName("order_id")
    val orderId: String,

    @SerialName("user_id")
    val userId: String,

    val amount: String,

    @SerialName("bank_code")
    val bankCode: String,

    @SerialName("account_number")
    val accountNumber: String,

    @SerialName("account_holder_name")
    val accountHolderName: String,

    val reason: String,

    @SerialName("proof_image_url")
    val proofImageUrl: String? = null,

    @SerialName("return_tracking_number")
    val returnTrackingNumber: String? = null,

    val status: String,

    @SerialName("admin_notes")
    val adminNotes: String? = null,

    @SerialName("xendit_payout_id")
    val xenditPayoutId: String? = null,

    @SerialName("created_at")
    val createdAt: Instant,

    @SerialName("updated_at")
    val updatedAt: Instant
)
package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailRefundDto(

    @SerialName("id")
    val id: String,

    @SerialName("order_id")
    val orderId: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("amount")
    val amount: Double,

    @SerialName("bank_code")
    val bankCode: String,

    @SerialName("account_number")
    val accountNumber: String,

    @SerialName("account_holder_name")
    val accountHolderName: String,

    @SerialName("reason")
    val reason: String,

    @SerialName("proof_image_url")
    val proofImageUrl: String? = null,

    @SerialName("return_tracking_number")
    val returnTrackingNumber: String? = null,

    @SerialName("status")
    val status: String,

    @SerialName("admin_notes")
    val adminNotes: String? = null,

    @SerialName("xendit_payout_id")
    val xenditPayoutId: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,

    @SerialName("street_line1")
    val streetLine1: String? = null,

    @SerialName("street_line2")
    val streetLine2: String? = null,

    @SerialName("city")
    val city: String? = null,

    @SerialName("district")
    val district: String? = null,

    @SerialName("sub_district")
    val subDistrict: String? = null,

    @SerialName("province_state")
    val provinceState: String? = null,

    @SerialName("postal_code")
    val postalCode: String? = null,

    @SerialName("country")
    val country: String? = null,
)

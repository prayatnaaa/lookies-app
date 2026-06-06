package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class PaymentAttemptDto(
    val id: String,
    @SerialName("order_id")
    val orderId: String,
    val provider: String,
    val channel: String,
    @SerialName("external_id")
    val externalId: String,
    val amount: Double,
    val currency: String,
    val status: String,
    @SerialName("failure_reason")
    val failureReason: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("payment_request_id")
    val paymentRequestId: String? = null,
    @SerialName("raw_response")
    val rawResponse: RawPaymentResponseDto? = null,
    @SerialName("redirect_url")
    val redirectUrl: String? = null
)

@Serializable
data class RawPaymentResponseDto(
    val id: String? = null,
    val status: String? = null,
    @SerialName("payment_method")
    val paymentMethod: RawPaymentMethodDto? = null
)

@Serializable
data class RawPaymentMethodDto(
    val type: String? = null,
    @SerialName("virtual_account")
    val virtualAccount: RawVirtualAccountDto? = null,
    @SerialName("qr_code")
    val qrCode: RawQrCodeDto? = null
)

@Serializable
data class RawQrCodeDto(
    @SerialName("channel_code")
    val channelCode: String? = null,
    @SerialName("channel_properties")
    val channelProperties: RawQrChannelPropertiesDto? = null
)

@Serializable
data class RawQrChannelPropertiesDto(
    @SerialName("qr_string")
    val qrString: String? = null,
    @SerialName("expires_at")
    val expiresAt: String? = null
)

@Serializable
data class RawVirtualAccountDto(
    @SerialName("channel_code")
    val channelCode: String? = null,
    @SerialName("channel_properties")
    val channelProperties: RawChannelPropertiesDto? = null
)

@Serializable
data class RawChannelPropertiesDto(
    @SerialName("expires_at")
    val expiresAt: String? = null,
    @SerialName("customer_name")
    val customerName: String? = null,
    @SerialName("virtual_account_number")
    val virtualAccountNumber: String? = null
)

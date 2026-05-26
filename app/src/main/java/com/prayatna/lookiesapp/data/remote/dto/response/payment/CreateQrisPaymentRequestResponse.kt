package com.prayatna.lookiesapp.data.remote.dto.response.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateQrisPaymentRequestResponse(
    val status: String,
    val message: String,
    val data: PaymentDataDto? = null
)

@Serializable
data class PaymentDataDto(
    val id: String,
    val country: String,
    val amount: Int,
    val currency: String,

    @SerialName("business_id")
    val businessId: String,

    @SerialName("reference_id")
    val referenceId: String,

    @SerialName("payment_method")
    val paymentMethod: PaymentMethodDto,

    val description: String? = null,
    val status: String,

    val created: String,
    val updated: String
)

@Serializable
data class PaymentMethodDto(
    val id: String,
    val type: String,

    @SerialName("reference_id")
    val referenceId: String,

    val status: String,

    @SerialName("qr_code")
    val qrCode: QrCodeDto? = null
)

@Serializable
data class QrCodeDto(
    val amount: Int,
    val currency: String,

    @SerialName("channel_code")
    val channelCode: String,

    @SerialName("channel_properties")
    val channelProperties: ChannelPropertiesDto
)
@Serializable
data class ChannelPropertiesDto(
    @SerialName("qr_string")
    val qrString: String
)

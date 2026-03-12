package com.prayatna.lookiesapp.data.remote.dto.response.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateXenditPaymentResponse(
    val status: String,
    val message: String,
    @SerialName("payment_attempt_id") val paymentAttemptId: String? = null,
    @SerialName("payment_token") val paymentToken: XenditPaymentTokenDto? = null
)

@Serializable
data class XenditPaymentTokenDto(

    @SerialName("payment_request_id")
    val paymentRequestId: String,

    val country: String,
    val currency: String,

    @SerialName("business_id")
    val businessId: String,

    @SerialName("reference_id")
    val referenceId: String,

    val created: String,
    val updated: String,
    val status: String,

    @SerialName("capture_method")
    val captureMethod: String,

    @SerialName("channel_code")
    val channelCode: String,

    @SerialName("customer_id")
    val customerId: String,

    @SerialName("request_amount")
    val requestAmount: Int,

    @SerialName("channel_properties")
    val channelProperties: TChannelPropertiesDto,

    val type: String,

    val actions: List<ActionDto>
)

@Serializable
data class TChannelPropertiesDto(

    @SerialName("success_return_url")
    val successReturnUrl: String,

    @SerialName("failure_return_url")
    val failureReturnUrl: String,

    @SerialName("cancel_return_url")
    val cancelReturnUrl: String,

    @SerialName("account_mobile_number")
    val accountMobileNumber: String
)

@Serializable
data class ActionDto(

    val type: String,
    val descriptor: String,
    val value: String
)
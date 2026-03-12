package com.prayatna.lookiesapp.data.remote.dto.request.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateXenditPaymentRequest(
    @SerialName("merchant_id") val merchantId: String,
    @SerialName("order_id") val orderId: String,
    @SerialName("reference_id") val referenceId: String,
    val type: String? = null,
    val country: String = "ID",
    val currency: String = "IDR",
    @SerialName("channel_code") val channelCode: String,
    @SerialName("channel_properties") val channelProperties: ChannelPropertiesDto,
    @SerialName("request_amount") val requestAmount: Double,
    @SerialName("capture_method") val captureMethod: String = "AUTOMATIC",
    @SerialName("customer_id") val customerId: String? = null
)

@Serializable
sealed interface ChannelPropertiesDto


@Serializable
@SerialName("CARD")
data class CardChannelProperties(
    @SerialName("mid_label") val midLabel: String? = null,
    @SerialName("skip_three_ds") val skipThreeDs: Boolean? = null,
    @SerialName("card_on_file_type") val cardOnFileType: String? = null,
    @SerialName("failure_return_url") val failureReturnUrl: String? = null,
    @SerialName("success_return_url") val successReturnUrl: String? = null,
    @SerialName("statement_descriptor") val statementDescriptor: String? = null,
    @SerialName("card_details") val cardDetails: CardDetails
) : ChannelPropertiesDto


@Serializable
data class CardDetails(
    @SerialName("card_number") val cardNumber: String,
    @SerialName("expiry_year") val expiryYear: String,
    @SerialName("expiry_month") val expiryMonth: String,
    @SerialName("cardholder_first_name") val cardholderFirstName: String,
    @SerialName("cardholder_last_name") val cardholderLastName: String,
    @SerialName("cardholder_email") val cardholderEmail: String
)


@Serializable
@SerialName("GOPAY")
data class GopayChannelProperties(
    @SerialName("account_mobile_number") val accountMobileNumber: String
) : ChannelPropertiesDto
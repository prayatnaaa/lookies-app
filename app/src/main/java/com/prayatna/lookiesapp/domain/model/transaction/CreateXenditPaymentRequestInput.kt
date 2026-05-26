package com.prayatna.lookiesapp.domain.model.transaction

data class CreateXenditPaymentRequestInput(
    val merchantId: String,
    val orderId: String,
    val referenceId: String,
    val type: String? = null,
    val country: String = "ID",
    val currency: String = "IDR",
    val channelCode: String,
    val channelProperties: ChannelProperties,
    val requestAmount: Double,
    val captureMethod: String = "AUTOMATIC",
    val customerId: String? = null
)

sealed interface ChannelProperties

data class CardChannelProperties(
    val midLabel: String? = null,
    val skipThreeDs: Boolean? = null,
    val cardOnFileType: String? = null,
    val failureReturnUrl: String? = null,
    val successReturnUrl: String? = null,
    val statementDescriptor: String? = null,
    val cardDetails: CardDetails
) : ChannelProperties

data class CardDetails(
    val cardNumber: String,
    val expiryYear: String,
    val expiryMonth: String,
    val cardholderFirstName: String,
    val cardholderLastName: String,
    val cardholderEmail: String
)


data class GopayChannelProperties(
    val accountMobileNumber: String
) : ChannelProperties
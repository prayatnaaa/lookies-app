package com.prayatna.lookiesapp.domain.model.transaction

data class CreateXenditPaymentRequestResult(
    val status: String,
    val message: String,
    val paymentAttemptId: String? = null,
    val paymentToken: XenditPaymentToken? = null
)

data class XenditPaymentToken(
    val paymentRequestId: String,
    val country: String,
    val currency: String,
    val businessId: String,
    val referenceId: String,
    val created: String,
    val updated: String,
    val status: String,
    val captureMethod: String,
    val channelCode: String,
    val customerId: String,
    val requestAmount: Int,
    val channelProperties: TChannelProperties,
    val type: String,
    val actions: List<Action>
)

data class TChannelProperties(
    val successReturnUrl: String,
    val failureReturnUrl: String,
    val cancelReturnUrl: String,
    val accountMobileNumber: String
)

data class Action(
    val type: String,
    val descriptor: String,
    val value: String
)
package com.prayatna.lookiesapp.domain.model.transaction

data class CreateQrisPaymentRequestResult(
    val status: String,
    val message: String,
    val data: QrisPaymentData? = null
)

data class QrisPaymentData(
    val id: String,
    val country: String,
    val amount: Int,
    val currency: String,
    val businessId: String,
    val referenceId: String,
    val paymentMethod: QrisPaymentMethod,
    val description: String?,
    val status: String,
    val created: String,
    val updated: String
)

data class QrisPaymentMethod(
    val id: String,
    val type: String,
    val referenceId: String,
    val status: String,
    val qrCode: QrCode?
)

data class QrCode(
    val amount: Int,
    val currency: String,
    val channelCode: String,
    val channelProperties: QrisChannelProperties
)

data class QrisChannelProperties(
    val qrString: String
)
package com.prayatna.lookiesapp.domain.model.admin

data class AdminTransactionDetail(
    val id: String,
    val totalAmount: Double,
    val status: String,
    val createdAt: String,
    val userEmail: String?,
    val userFullName: String?,
    val items: List<AdminOrderItem>,
    val splits: List<AdminOrderSplit>,
    val paymentAttempts: List<AdminPaymentAttempt>,
    val shipments: List<AdminShipment>,
    val refundRequests: List<AdminRefundRequest>
)

data class AdminOrderItem(
    val id: String,
    val itemType: String,
    val unitPrice: Double,
    val quantity: Int,
    val subtotal: Double,
    val eventId: Int?,
    val eventPaintingId: String?
)

data class AdminOrderSplit(
    val id: String,
    val merchantId: String,
    val grossAmount: Double,
    val platformFee: Double,
    val netAmount: Double,
    val payoutStatus: String
)

data class AdminPaymentAttempt(
    val status: String,
    val provider: String,
    val channel: String,
    val externalId: String,
    val createdAt: String
)

data class AdminShipment(
    val id: String,
    val trackingNumber: String?,
    val status: String,
    val shippingCost: Double
)

data class AdminRefundRequest(
    val id: String,
    val status: String,
    val amount: Double,
    val reason: String
)

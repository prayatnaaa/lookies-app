package com.prayatna.lookiesapp.domain.model.transaction

data class Shipment(
    val id: String,
    val merchantId: String,
    val orderId: String,
    val trackingNumber: String,
    val status: String,
    val shippingCost: Double,
    val createdAt: String,
    val shippedAt: String? = null,
    val reciepentName: String,
    val phoneNumber: String,
    val addressLine: String,
    val province: String,
)
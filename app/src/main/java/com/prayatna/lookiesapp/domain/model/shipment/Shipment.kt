package com.prayatna.lookiesapp.domain.model.shipment

data class Shipment(
    val id: String,
    val merchantId: String,
    val artistId: String? = null,
    val orderId: String,
    val trackingNumber: String? = null,
    val status: String,
    val shippingCost: Double,
    val createdAt: String,
    val shippedAt: String? = null,
    val reciepentName: String,
    val phoneNumber: String,
    val addressLine: String,
    val province: String,
    val postalCode: String,
    val arrivalProofUrl: String? = null
)
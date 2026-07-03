package com.prayatna.lookiesapp.domain.model.shipment

import android.util.Log
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit


fun Shipment.isPendingMoreThan3Days(): Boolean {
    if (status.lowercase() != "pending") return false

    return try {
        val normalizedDate = createdAt.replace(" ", "T")

        val created = OffsetDateTime.parse(normalizedDate)

        val days = ChronoUnit.DAYS.between(
            created.toInstant(),
            java.time.Instant.now()
        )

        Log.d("SHIPMENT-DATE", "created=$normalizedDate days=$days")

        days >= 3
    } catch (e: Exception) {
        Log.e("SHIPMENT-DATE", "Failed to parse: $createdAt", e)
        false
    }
}

fun Shipment.canBeRefunded(): Boolean {
    val statusLower = status.lowercase()
    return statusLower == "delivered" || isPendingMoreThan3Days()
}

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
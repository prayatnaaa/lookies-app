package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdminTransactionDto(
    val id: String,
    @SerialName("total_amount") val totalAmount: Double,
    val status: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("users") val user: OrderUserDto? = null,
    // Mengambil status pembayaran terakhir saja untuk list
    @SerialName("payment_attempts") val paymentAttempts: List<PaymentAttemptSimpleDto> = emptyList()
)

@Serializable
data class PaymentAttemptSimpleDto(
    val status: String
)

@Serializable
data class AdminTransactionDetailDto(
    val id: String,
    @SerialName("total_amount") val totalAmount: Double,
    val status: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("users") val user: OrderUserDto? = null,
    @SerialName("order_items") val items: List<OrderItemDetailDto> = emptyList(),
    @SerialName("order_splits") val splits: List<OrderSplitDto> = emptyList(),
    @SerialName("payment_attempts") val paymentAttempts: List<PaymentAttemptDetailDto> = emptyList(),
    @SerialName("shipments") val shipments: List<ShipmentDto> = emptyList(), // Jika ada karya seni fisik
    @SerialName("refund_requests") val refundRequests: List<RefundRequestDto> = emptyList()
)


@Serializable
data class OrderUserDto(
    val email: String,
    @SerialName("user_profiles") val profile: UserProfileDto? = null
)

@Serializable
data class UserProfileDto(
    @SerialName("full_name") val fullName: String? = null,
)

@Serializable
data class OrderItemDetailDto(
    val id: String,
    @SerialName("item_type") val itemType: String,
    @SerialName("unit_price") val unitPrice: Double,
    val quantity: Int,
    val subtotal: Double,
    @SerialName("event_id") val eventId: Int? = null,
    @SerialName("event_painting_id") val eventPaintingId: String? = null
)
@Serializable
data class PaymentAttemptDetailDto(
    val status: String,
    val provider: String,
    val channel: String,
    @SerialName("external_id") val externalId: String,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class RefundRequestDto(
    val id: String,
    val status: String,
    val amount: Double,
    val reason: String
)
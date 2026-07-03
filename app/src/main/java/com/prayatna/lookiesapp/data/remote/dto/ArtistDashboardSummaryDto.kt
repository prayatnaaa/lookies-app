package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDashboardSummaryDto(

    @SerialName("business_id")
    val businessId: String,

    val status: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("total_paintings")
    val totalPaintings: Int,

    @SerialName("total_catalog_value")
    val totalCatalogValue: Double,

    @SerialName("active_event_paintings")
    val activeEventPaintings: Int,

    @SerialName("total_sold_paintings")
    val totalSoldPaintings: Int,

    @SerialName("conversion_rate_percent")
    val conversionRatePercent: Double,

    @SerialName("total_reviews")
    val totalReviews: Int,

    @SerialName("avg_rating")
    val avgRating: Double,

    @SerialName("total_orders")
    val totalOrders: Int,

    @SerialName("total_revenue")
    val totalRevenue: Double,

    @SerialName("pending_payout")
    val pendingPayout: Double,

    @SerialName("total_paid_out")
    val totalPaidOut: Double,

    @SerialName("current_month_revenue")
    val currentMonthRevenue: Double,

    @SerialName("orders_to_ship")
    val ordersToShip: Int
)
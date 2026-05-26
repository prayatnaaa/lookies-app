package com.prayatna.lookiesapp.domain.model.artist

data class ArtistDashboardSummary(
    val businessId: String,
    val userId: String,
    val totalPaintings: Int,
    val totalCatalogValue: Double,
    val activeEventPaintings: Int,
    val totalSoldPaintings: Int,
    val conversionRatePercent: Double,
    val totalReviews: Int,
    val avgRating: Double,
    val totalOrders: Int,
    val totalRevenue: Double,
    val pendingPayout: Double,
    val totalPaidOut: Double,
    val currentMonthRevenue: Double,
    val ordersToShip: Int
)
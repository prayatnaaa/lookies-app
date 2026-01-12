package com.prayatna.lookiesapp.domain.model.artist

data class ArtistDashboardSummary(
    val artistId: String,
    val fullName: String?,
    val totalPaintings: Int,
    val paintingsSold: Int,
    val paintingsOnSale: Int,
    val totalEarnings: Double,
    val pendingPayout: Double,
    val averageRating: Double
)

data class ArtistMonthlySales(
    val monthYear: String,
    val totalTransactions: Int,
    val totalIncome: Double
)

data class ArtistDashboardData(
    val summary: ArtistDashboardSummary,
    val monthlySales: List<ArtistMonthlySales>
)
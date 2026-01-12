package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDashboardSummaryDto(
    @SerialName("artist_id") val artistId: String,
    @SerialName("full_name") val fullName: String?,
    @SerialName("total_paintings_created") val totalPaintings: Int,
    @SerialName("paintings_sold") val paintingsSold: Int,
    @SerialName("paintings_on_sale") val paintingsOnSale: Int,
    @SerialName("total_earnings_all_time") val totalEarnings: Double,
    @SerialName("pending_payout") val pendingPayout: Double,
    @SerialName("average_rating") val averageRating: Double
)

@Serializable
data class ArtistMonthlySalesDto(
    @SerialName("month_year") val monthYear: String,
    @SerialName("total_transactions") val totalTransactions: Int,
    @SerialName("total_income") val totalIncome: Double
)

data class ArtistDashboardDataDto(
    val summary: ArtistDashboardSummaryDto,
    val monthlySales: List<ArtistMonthlySalesDto>
)
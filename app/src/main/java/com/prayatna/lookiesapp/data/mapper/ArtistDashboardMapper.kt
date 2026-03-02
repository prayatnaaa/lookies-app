package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.ArtistDashboardSummaryDto
import com.prayatna.lookiesapp.domain.model.artist.ArtistDashboardSummary

fun ArtistDashboardSummaryDto.toDomain(): ArtistDashboardSummary {
    return ArtistDashboardSummary(
        userId = userId,
        totalPaintings = totalPaintings,
        totalCatalogValue = totalCatalogValue,
        activeEventPaintings = activeEventPaintings,
        totalSoldPaintings = totalSoldPaintings,
        totalReviews = totalReviews,
        avgRating = avgRating,
        totalOrders = totalOrders,
        totalRevenue = totalRevenue,
        pendingPayout = pendingPayout,
        totalPaidOut = totalPaidOut,
        currentMonthRevenue = currentMonthRevenue,
        ordersToShip = ordersToShip,
        conversionRatePercent = conversionRatePercent
    )
}
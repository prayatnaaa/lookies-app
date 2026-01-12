package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.ArtistDashboardDataDto
import com.prayatna.lookiesapp.data.remote.dto.ArtistDashboardSummaryDto
import com.prayatna.lookiesapp.data.remote.dto.ArtistMonthlySalesDto
import com.prayatna.lookiesapp.domain.model.artist.ArtistDashboardData
import com.prayatna.lookiesapp.domain.model.artist.ArtistDashboardSummary
import com.prayatna.lookiesapp.domain.model.artist.ArtistMonthlySales

fun ArtistDashboardSummaryDto.toDomain(): ArtistDashboardSummary {
    return ArtistDashboardSummary(
        artistId = artistId,
        fullName = fullName,
        totalPaintings = totalPaintings,
        paintingsSold = paintingsSold,
        paintingsOnSale = paintingsOnSale,
        totalEarnings = totalEarnings,
        pendingPayout = pendingPayout,
        averageRating = averageRating
    )
}

fun ArtistMonthlySalesDto.toDomain(): ArtistMonthlySales {
    return ArtistMonthlySales(
        monthYear = monthYear,
        totalTransactions = totalTransactions,
        totalIncome = totalIncome
    )
}

fun ArtistDashboardDataDto.toDomain(): ArtistDashboardData {
    return ArtistDashboardData(
        summary = summary.toDomain(),
        monthlySales = monthlySales.map { it.toDomain() }
    )
}
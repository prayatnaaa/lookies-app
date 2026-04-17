package com.prayatna.lookiesapp.domain.usecase.artist

import com.prayatna.lookiesapp.domain.model.artist.ArtistDashboardSummary
import com.prayatna.lookiesapp.domain.repository.ArtistRepository
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArtistDashboardSummaryUseCase @Inject constructor(
    private val repository: ArtistRepository
) {
    operator fun invoke(): Flow<ArtistDashboardSummary> {
        return repository.getDashboardData()
    }
}

package com.prayatna.lookiesapp.domain.usecase.painting

import android.util.Log
import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.domain.model.painting.PaintingFilter
import com.prayatna.lookiesapp.domain.model.painting.SortType
import com.prayatna.lookiesapp.domain.repository.ArtistRepository
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetArtistPaintingsUseCase @Inject constructor(
    private val paintingRepository: PaintingRepository,
    private val artistRepository: ArtistRepository
) {

    suspend operator fun invoke(
        status: String? = null,
        filter: PaintingFilter? = null,
        id: String? = null
    ): DataResult<List<Painting>> {

        val businessId = id
            ?: when (val accountResult = artistRepository.getArtistBusinessId()) {
                is DataResult.Success -> accountResult.data.businessId
                is DataResult.Error -> return DataResult.Error(accountResult.error)
                is DataResult.Loading -> return DataResult.Loading
                is DataResult.Idle -> return DataResult.Idle
            }

        return when (val paintingResult = paintingRepository.getPaintingsByArtist(businessId, status)) {
            is DataResult.Success -> {
                var list = paintingResult.data
                Log.d("PaintingStatus", list.toString())

                if (filter != null) {
                    list = list.filter { painting ->
                        val matchArtist = filter.artistId?.let { painting.artistId == it } ?: true
                        val matchMedium = filter.medium?.let { painting.medium.equals(it, ignoreCase = true) } ?: true
                        val matchStyle = filter.artStyle?.let { painting.artStyle?.equals(it, ignoreCase = true) == true } ?: true
                        val matchSubject = filter.subject?.let { painting.subject?.equals(it, ignoreCase = true) == true } ?: true
                        val matchMinYear = filter.minYear?.let { painting.yearCreated >= it } ?: true
                        val matchMaxYear = filter.maxYear?.let { painting.yearCreated <= it } ?: true

                        matchArtist && matchMedium && matchStyle && matchSubject && matchMinYear && matchMaxYear
                    }

                    filter.sortBy?.let { sort ->
                        list = when (sort) {
                            SortType.YEAR_ASC -> list.sortedBy { it.yearCreated }
                            SortType.YEAR_DESC -> list.sortedByDescending { it.yearCreated }
                            SortType.NAME -> list.sortedBy { it.title.lowercase() }
                            SortType.CREATED_AT -> list.sortedByDescending { it.createdAt }
                        }
                    }
                }

                DataResult.Success(list)
            }

            is DataResult.Error -> paintingResult
            is DataResult.Loading -> DataResult.Loading
            is DataResult.Idle -> DataResult.Idle
        }
    }
}
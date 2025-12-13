package com.prayatna.lookiesapp.domain.usecase.painting

import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.domain.model.painting.PaintingFilter
import com.prayatna.lookiesapp.domain.model.painting.SortType
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetArtistPaintingsUseCase @Inject constructor(
    private val paintingRepository: PaintingRepository
) {

    suspend operator fun invoke(filter: PaintingFilter, id: String): DataResult<List<Painting>> {
        return when (val result = paintingRepository.getPaintingsByArtist(id)) {
            is DataResult.Success -> {
                var list = result.data

                filter.artistId?.let {
                    list = list.filter { painting -> painting.artistId == it }
                }

                filter.medium?.let {
                    list = list.filter { painting -> painting.medium.equals(it, ignoreCase = true) }
                }

                filter.artStyle?.let {
                    list = list.filter { painting -> painting.artStyle?.equals(it, ignoreCase = true) == true }
                }

                filter.subject?.let {
                    list = list.filter { painting -> painting.subject?.equals(it, ignoreCase = true) == true }
                }

                filter.minYear?.let {
                    list = list.filter { painting -> painting.yearCreated >= it }
                }

                filter.maxYear?.let {
                    list = list.filter { painting -> painting.yearCreated <= it }
                }

                filter.sortBy?.let { sort ->
                    list = when (sort) {
                        SortType.YEAR_ASC -> list.sortedBy { it.yearCreated }
                        SortType.YEAR_DESC -> list.sortedByDescending { it.yearCreated }
                        SortType.NAME -> list.sortedBy { it.title.lowercase() }
                        SortType.CREATED_AT -> list.sortedByDescending { it.createdAt }
                    }
                }

                DataResult.Success(list)
            }

            is DataResult.Error -> result
            is DataResult.Loading -> DataResult.Loading
            is DataResult.Idle -> DataResult.Idle
        }
    }
}
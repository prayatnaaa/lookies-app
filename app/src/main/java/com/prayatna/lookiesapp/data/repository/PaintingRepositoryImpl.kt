package com.prayatna.lookiesapp.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabasePaintingService
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.painting.AddPaintingParams
import com.prayatna.lookiesapp.domain.model.painting.CreatePaintingReviewInput
import com.prayatna.lookiesapp.domain.model.painting.DetailPainting
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.domain.model.painting.PaintingAttribute
import com.prayatna.lookiesapp.domain.model.painting.PaintingReview
import com.prayatna.lookiesapp.domain.model.painting.UploadPaintingOutput
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.compressImage
import com.prayatna.lookiesapp.utils.extractSupabaseError
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PaintingRepositoryImpl @Inject constructor(
    private val paintingService: SupabasePaintingService,
    @ApplicationContext private val context: Context
): PaintingRepository {
    override suspend fun getPaintings(
        id: String?,
        status: String?,
        eventId: String?,
        showSelfPaintings: Boolean,
        limitCount: Long?
    ): DataResult<List<EventPainting>> {
        return try {
            val response = paintingService.getPaintings(
                id = id,
                status = status,
                eventId = eventId,
                showSelfPaintings = showSelfPaintings,
                limitCount = limitCount
            )
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            Log.e("PaintingService", e.message.toString())
            DataResult.Error(e.message ?: "Something went wrong! Please check your connection")
        }
    }

    override suspend fun getEventPaintingDetail(id: String): DataResult<EventPainting> {
        return try {
            val response = paintingService.getEventPaintingDetail(id = id)
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Something went wrong!")
        }
    }

    override suspend fun getPaintingsByArtist(id: String): DataResult<List<Painting>> {
        return try {
            val response = paintingService.getPaintingByArtistId(id)
            Log.d("PaintingRepository", response.toString())
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Something went wrong! Please check your connection")
        }
    }

    override suspend fun getPaintingDetail(id: Int): DataResult<DetailPainting> {
        return  try {
            withContext(Dispatchers.IO) {
                val result = paintingService.getPaintingDetail(id = id)
                DataResult.Success(result.toDomain())
            }
        } catch (e: RestException) {
            DataResult.Error(e.message ?: "Something went wrong")
        }
    }

    override suspend fun uploadPainting(painting: AddPaintingParams, image: Uri): DataResult<UploadPaintingOutput> {
        val compressedImage = image.compressImage(context, 500_000L)
        return try {
            val response = paintingService.uploadPainting(painting = painting.toDto(), image = compressedImage)
            DataResult.Success(response.toDomain())
        } catch (e: Exception) {
            Log.d("UPLOAD-PAINTING", e.message.toString())
            DataResult.Error(e.message ?: "Something went wrong! Please check your connection")
        }
    }

    override suspend fun deletePainting(paintingId: String): DataResult<String> {
        return try {
            val response = paintingService.deletePaintingById(paintingId)
            DataResult.Success(response)
        } catch (e: RestException) {
            DataResult.Error(e.message ?: "Something went wrong!")
        }
    }



    override suspend fun editPainting(painting: Painting): DataResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getPaintingArtStyles(): DataResult<List<PaintingAttribute>> {
        return try {
            val response = paintingService.getPaintingArtStyles()
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.message ?: "Something went wrong!")
        }
    }

    override suspend fun getPaintingMediums(): DataResult<List<PaintingAttribute>> {
        return try {
            val response = paintingService.getPaintingMediums()
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.message ?: "Something went wrong!")
        }
    }

    override suspend fun updateEventPaintingStatus(eventPaintingId: String, status: String): DataResult<Unit> {
        return try {
            paintingService.updateEventPaintingStatus(eventPaintingId, status)
            DataResult.Success(Unit)
        } catch (e: RestException) {
            DataResult.Error(e.message ?: "Something went wrong!")
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Something went wrong!")
        }
    }

    override suspend fun createPaintingReview(paintingReview: CreatePaintingReviewInput): DataResult<PaintingReview> {
        return try {
            val result = paintingService.createPaintingReview(paintingReview.toDto())
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Something went wrong!")
        }
    }
}
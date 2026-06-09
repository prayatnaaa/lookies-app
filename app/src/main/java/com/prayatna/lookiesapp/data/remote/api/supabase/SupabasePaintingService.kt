package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.data.remote.dto.EventPaintingDto
import com.prayatna.lookiesapp.data.remote.dto.PaintingReviewDto
import com.prayatna.lookiesapp.data.remote.dto.request.painting.CreatePaintingReviewRequest
import com.prayatna.lookiesapp.data.remote.dto.request.painting.UpdatePaintingRequest
import com.prayatna.lookiesapp.data.remote.dto.request.painting.UploadPaintingRequest
import com.prayatna.lookiesapp.data.remote.dto.response.painting.BasePaintingDto
import com.prayatna.lookiesapp.data.remote.dto.response.painting.GetDetailPaintingDto
import com.prayatna.lookiesapp.data.remote.dto.response.painting.GetPaintingDto
import com.prayatna.lookiesapp.data.remote.dto.response.painting.PaintingAttributeResponse
import com.prayatna.lookiesapp.data.remote.dto.response.painting.UploadPaintingResponse
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.Storage
import java.util.UUID
import javax.inject.Inject

class SupabasePaintingService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val storage: Storage,
) {

    suspend fun getPaintingReviewByEventPaintingId(eventPaintingId: String): PaintingReviewDto? {
        return postgrest["painting_reviews"].select {
            filter {
                eq("event_painting_id", eventPaintingId)
            }
        }.decodeSingleOrNull()
    }
    suspend fun getPaintings(
        id: String?,
        status: String?,
        eventId: String?,
        showSelfPaintings: Boolean = false,
        limitCount: Long? = null
    ): List<EventPaintingDto> {
        val user = auth.currentUserOrNull() ?:
        throw IllegalStateException("User not logged in")
        val result = postgrest.from("event_paintings_view")
            .select {
                if (limitCount != null) {
                    limit(count = limitCount)
                }
                filter {
                    if (eventId != null) {
                        eq("event_id", eventId)
                    }
                    if (status != null) {
                        val statuses = status.split(",").map { it.trim() }
                        isIn("status", statuses)
                    }
                    if (id != null) {
                        eq("id", id)
                    }
                    eq("status", "accepted")
                    if (!showSelfPaintings) {
                        neq("artist_user_id", user.id)
                    }
                }
            }
            .decodeList<EventPaintingDto>()
        Log.d("PaintingService", "getPaintings: $result")
        return result
    }

    suspend fun getPaintingDetail(id: Int): GetDetailPaintingDto {
        val result = postgrest.from("detail_paintings").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<GetDetailPaintingDto>()
        return result
    }

    suspend fun getEventPaintingDetail(id: String): EventPaintingDto {
        val result = postgrest.from("event_paintings_view").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<EventPaintingDto>()

        return result
    }

    suspend fun getPaintingByArtistId(id: String, status: String? = null): List<GetPaintingDto> {
        return postgrest
            .from("painting_view")
            .select {
                filter {
                    eq("artist_id", id)
                    if (status != null) {
                        eq("availability_status", status)
                    }
                }
            }
            .decodeList<GetPaintingDto>()
    }

    suspend fun createPaintingReview(review: CreatePaintingReviewRequest): PaintingReviewDto {
        val user = auth.currentUserOrNull() ?: throw IllegalStateException("User not logged in")
        val finalReq = review.copy(
            userId = user.id
        )
        return postgrest.from("painting_reviews").insert(finalReq) {
            select()
        }.decodeSingle<PaintingReviewDto>()
    }


    suspend fun getPaintingArtStyles(): List<PaintingAttributeResponse> {
            val artStyles = postgrest
                .from("painting_art_styles")
                .select()
                .decodeList<PaintingAttributeResponse>()
        return artStyles
    }

    suspend fun getPaintingMediums(): List<PaintingAttributeResponse> {
        val artStyles = postgrest
            .from("painting_mediums")
            .select()
            .decodeList<PaintingAttributeResponse>()
        return artStyles
    }

    suspend fun uploadPainting(painting: UploadPaintingRequest, image: ByteArray?): UploadPaintingResponse {
        auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("User not logged in")

        var uploadedPath: String? = null
        if (image == null) throw IllegalArgumentException("Image cannot be null")

        try {
            val path = "${painting.artistId}/${UUID.randomUUID()}.png"
            storage.from("paintings").upload(
                path = path,
                data = image,
                upsert = false
            )

            uploadedPath = path
            val imageUrl = Helper.buildImageUrl(imageName = path, bucketName = "paintings")

            val finalPainting = painting.copy(
                paintingUrl = imageUrl
            )

            return postgrest.from("paintings").insert(finalPainting) {
                select(Columns.list("id", "title"))
            }.decodeSingle()

        } catch (e: Exception) {
            uploadedPath?.let {
                storage.from("paintings").delete(it)
            }
            throw e
        }
    }

    private fun extractStoragePath(url: String): String {
        return url.substringAfter("/object/public/paintings/")
    }

    private suspend fun deletePainting(paintingId: Int, paintingUrl: String) {
            postgrest.from("paintings")
                .delete {
                    filter { eq("id", paintingId) }
                }

            val path = extractStoragePath(paintingUrl)
            storage.from("paintings").delete(path)
    }

    suspend fun deletePaintingById(paintingId: String): String {
        val painting = postgrest.from("paintings")
            .select { filter { eq("id", paintingId) } }
            .decodeSingle<GetPaintingDto>()
        deletePainting(paintingId = paintingId.toInt(), paintingUrl = painting.paintingUrl)
        return "Painting delete successfully"
    }

    suspend fun updateEventPaintingStatus(eventPaintingId: String, status: String) {
        postgrest.from("event_paintings")
            .update(mapOf("status" to status, "updated_at" to "now()")) {
                filter { eq("id", eventPaintingId) }
            }
    }

    suspend fun updatePainting(painting: UploadPaintingRequest, paintingId: Int, image: ByteArray?): BasePaintingDto {
        auth.currentUserOrNull()?.id ?: throw IllegalStateException("User not logged in")

        Log.d("EDIT-PAINTING", paintingId.toString())

        var uploadedPath: String? = null
        try {
            val imageUrl = if (image != null) {
                val path = "${painting.artistId}/${UUID.randomUUID()}.png"
                storage.from("paintings").upload(
                    path = path,
                    data = image,
                    upsert = false
                )
                uploadedPath = path
                Helper.buildImageUrl(imageName = path, bucketName = "paintings")
            } else {
                null
            }

            val request = UpdatePaintingRequest(
                title = painting.title,
                paintingUrl = imageUrl, // Only update if new image
                description = painting.description,
                dimensionHeight = painting.dimensionHeight,
                dimensionWidth = painting.dimensionWidth,
                medium = painting.medium,
                artStyle = painting.artStyle,
                subject = painting.subject,
                yearCreated = painting.yearCreated,
                price = painting.price
            )

            val response = postgrest.from("paintings").update(request) {
                select()
                filter { eq("id", paintingId) }
            }.decodeSingle<BasePaintingDto>()

            return response

        } catch (e: Exception) {
            uploadedPath?.let {
                storage.from("paintings").delete(it)
            }
            throw e
        }
    }
}
package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.request.painting.UploadPaintingRequest
import com.prayatna.lookiesapp.data.remote.dto.response.painting.GetDetailPaintingDto
import com.prayatna.lookiesapp.data.remote.dto.response.painting.GetPaintingDto
import com.prayatna.lookiesapp.data.remote.dto.response.painting.PaintingAttributeResponse
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import java.util.UUID
import javax.inject.Inject

class SupabasePaintingService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val storage: Storage,
) {

    suspend fun getPaintings(): List<GetPaintingDto> {
        val result = postgrest.from("paintings")
            .select()
            .decodeList<GetPaintingDto>()
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

    suspend fun getPaintingByArtistId(id: String?): List<GetPaintingDto> {
        val artistId = id ?: auth.currentUserOrNull()?.id
        ?: throw IllegalStateException("Artist ID is required")

        return postgrest
            .from("paintings")
            .select {
                filter {
                    eq("artist_id", artistId)
                }
            }
            .decodeList<GetPaintingDto>()
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

    suspend fun uploadPainting(painting: UploadPaintingRequest, image: ByteArray?): GetPaintingDto {
        val artistId = auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("User not logged in")

        var uploadedPath: String? = null
        if (image == null) throw IllegalArgumentException("Image cannot be null")

        try {
            val path = "${artistId}/${UUID.randomUUID()}.png"
            storage.from("paintings").upload(
                path = path,
                data = image,
                upsert = true
            )

            uploadedPath = path
            val imageUrl = Helper.buildImageUrl(imageName = path, bucketName = "paintings")

            val finalPainting = painting.copy(
                artistId = artistId,
                paintingUrl = imageUrl
            )

            return postgrest.from("paintings").insert(finalPainting) {
                select()
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
}
package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.remote.dto.PaintingDto
import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.domain.repository.ArtistRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.Helper
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val storage: Storage,
): ArtistRepository {

    override suspend fun publishPainting(painting: Painting, imageFile: ByteArray): DataResult<String> {
        return try {
            if (imageFile.isNotEmpty()) {
                val imageUrl =
                    storage.from("painting_urls").upload(
                        path = "${painting.title}.png",
                        data = imageFile,
                        upsert = true
                    )

                val paintingDto = PaintingDto(
                    artistId = painting.artistId,
                    title = painting.title,
                    thumbnailImageUrl = Helper.buildImageUrl(imageName = imageUrl, bucketName = "painting_urls")
                )

                postgrest.from("paintings")
                    .insert(paintingDto)

                DataResult.Success("Your painting have been published!")

            } else {
                DataResult.Error("Something went wrong! Please check every field")
            }
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message ?: "Network error")
        } catch (e: Exception) {
            DataResult.Error("Something went wrong! Please check your connection")
        }
    }

}
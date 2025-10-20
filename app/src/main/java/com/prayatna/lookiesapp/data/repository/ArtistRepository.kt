package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.data.remote.dto.ArtistApplicationDto
import com.prayatna.lookiesapp.data.remote.dto.PaintingDto
import com.prayatna.lookiesapp.data.model.ArtistApplication
import com.prayatna.lookiesapp.data.model.Painting
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface ArtistRepository {
    suspend fun artistApplication(artistApplication: ArtistApplication): DataResult<String>
    suspend fun publishPainting(painting: Painting, imageFile: ByteArray): DataResult<String>
}

class ArtistRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val storage: Storage,
    private val userPreference: UserPreference
): ArtistRepository {
    override suspend fun artistApplication(artistApplication: ArtistApplication): DataResult<String> {
        val userId = userPreference.userIdPreference.first()
        val artistApplicationDto = ArtistApplicationDto(
            userId = userId as String,
            portoUrl = artistApplication.portoUrl,
            motiveLetter = artistApplication.motiveLetter,
            status = artistApplication.status,
            reviewerId = artistApplication.reviewerId,
            reviewNote = artistApplication.reviewNote,
            businessType = artistApplication.businessType
        )

        return try {
            postgrest
                .from("artist_applications")
                .insert(artistApplicationDto)

            DataResult.Success("Your application have been submitted!")

        } catch (e: SupabaseEncodingException) {
            DataResult.Error("Something went wrong! ${e.localizedMessage}")
        } catch (e: Exception) {
            DataResult.Error("Error! ${e.localizedMessage}")
        }
    }

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
        } catch (e: SupabaseEncodingException) {
            DataResult.Error("Something went wrong! ${e.localizedMessage}")
        } catch (e: Exception) {
            DataResult.Error("Something went wrong! ${e.localizedMessage}")
        }
    }

}
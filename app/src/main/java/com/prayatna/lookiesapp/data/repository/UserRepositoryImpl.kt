package com.prayatna.lookiesapp.data.repository

import android.content.Context
import android.net.Uri
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.domain.model.user.User
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseUserService
import com.prayatna.lookiesapp.data.remote.dto.ProfileDto
import com.prayatna.lookiesapp.data.remote.mapper.asDomainModel
import com.prayatna.lookiesapp.data.remote.mapper.toDto
import com.prayatna.lookiesapp.domain.repository.UserRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.Helper
import com.prayatna.lookiesapp.utils.compressImage
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val storage: Storage,
    private val userPreference: UserPreference,
    private val supabaseUserService: SupabaseUserService,
    @ApplicationContext private val context: Context
): UserRepository {

    override suspend fun getUser(): DataResult<User> {
       return try {
           val response = supabaseUserService.getUser()
           val user =  response.asDomainModel()
           DataResult.Success(user)
       } catch (e: RestException) {
           DataResult.Error(e.error)
       } catch (e: Exception) {
           DataResult.Error(e.message.toString())
       }
    }

    override suspend fun submitPartnerApplication(
        partnerName: String,
        partnerType: String,
        locationId: Int,
        portfolioLink: String,
        imageLogo: Uri
    ): DataResult<String> {
        return try {

            val compressImage = imageLogo.compressImage(
                context = context,
                compressionThreshold = 500_000L
            )

            if (compressImage == null) {
                return DataResult.Error("Image is not selected")
            }
            val response = supabaseUserService.submitPartnerApplication(
                partnerName = partnerName,
                partnerType = partnerType,
                locationId = locationId,
                portfolioLink = portfolioLink,
                imageLogo = compressImage
            )
            DataResult.Success(response)
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message.toString())
        }
    }

    override fun getProfile(): Flow<DataResult<ProfileDto>> =
        userPreference.getProfile()
            .map { localProfile ->
                if (localProfile.id.isEmpty()) {
                    DataResult.Loading
                } else {
                    DataResult.Success(localProfile.toDto())
                }
            }
            .onStart {
                try {
                    val userId = auth.currentUserOrNull()?.id ?: return@onStart

                    val remoteProfile = postgrest
                        .from("user_profiles")
                        .select {
                            filter { eq("user_id", userId) }
                        }
                        .decodeSingle<ProfileDto>()

                    userPreference.setProfile(remoteProfile.asDomainModel())

                } catch (e: Exception) {
                    emit(DataResult.Error("Something went wrong: ${e.localizedMessage}"))
                }
            }


    override suspend fun editProfile(fullName: String, bio: String, address: String, username: String): DataResult<String> {
        return try {

            val userId = auth.currentUserOrNull()?.id ?: return DataResult.Error("You are not authenticated")
            val result = postgrest.from("user_profiles")
                .update({
                    set("full_name", fullName)
                    set("bio", bio)
                    set("address", address)
                    set("username", username)
                }) {
                    filter {
                        eq("user_id", userId)
                    }
                }
            DataResult.Success(result.data)
        } catch (e: SupabaseEncodingException) {
            DataResult.Error("Error! ${e.message}")
        } catch (e: Exception) {
            DataResult.Error("Error! ${e.message}")
        }
    }

    override suspend fun editProfileImage(image: ByteArray): DataResult<String> {
        return try {
            if (image.isNotEmpty()) {
                val path = UUID.randomUUID()
                val imageUrl =
                    storage.from("user_profile_image")
                        .upload(
                            path = "${path}.png",
                            data = image,
                            upsert = true
                        )

                val result = postgrest.from("user_profiles")
                    .update(
                        {
                            set("profile_picture_url", Helper.buildImageUrl(imageName = imageUrl, bucketName = "user_profile_image"))
                        }
                    )
                DataResult.Success(result.data)
            } else {
                DataResult.Error(error = "Image is not selected")
            }
        } catch (e: SupabaseEncodingException) {
            DataResult.Error(e.message.toString())
        } catch (e: Exception) {
            DataResult.Error(e.message.toString())
        }
    }

}
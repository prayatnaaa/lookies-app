package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.data.remote.dto.ProfileDto
import com.prayatna.lookiesapp.data.remote.mapper.asDomainModel
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

interface UserRepository {
    fun getProfile(): Flow<DataResult<ProfileDto>>
    suspend fun editProfile(fullName: String, bio: String, address: String, username: String): DataResult<String>
    suspend fun editProfileImage(image: ByteArray): DataResult<String>
}

class UserRepositoryImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val storage: Storage,
    private val userPreference: UserPreference
): UserRepository {
    override fun getProfile(): Flow<DataResult<ProfileDto>> = flow {
        try {
            emit(DataResult.Loading)
            val userId = auth.currentUserOrNull()?.id
            if (userId == null) {
                emit(DataResult.Error("User not logged in"))
                return@flow
            }

            val result = postgrest
                .from(table = "user_profiles")
                .select {
                    filter {
                        eq("user_id", userId.toString())
                    }
                }
                .decodeSingle<ProfileDto>()

            userPreference.setProfile(result.asDomainModel())
            emit(DataResult.Success(result))

        } catch (e: SupabaseEncodingException) {
            emit(DataResult.Error("Something went wrong: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(DataResult.Error("Something went wrong! Please check your connection!"))
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
                            set("profile_picture_url", Helper.buildImageUrl(imageName = imageUrl))
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
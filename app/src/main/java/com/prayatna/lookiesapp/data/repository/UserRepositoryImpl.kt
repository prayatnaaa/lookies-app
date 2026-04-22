package com.prayatna.lookiesapp.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.messaging.FirebaseMessaging
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.data.mapper.asDomainModel
import com.prayatna.lookiesapp.data.mapper.toDto
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseUserService
import com.prayatna.lookiesapp.data.remote.dto.ProfileDto
import com.prayatna.lookiesapp.data.remote.dto.response.user.RoleApplicationResponse
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.user.ArtistApplicationInput
import com.prayatna.lookiesapp.domain.model.user.CreateUserAddressInput
import com.prayatna.lookiesapp.domain.model.user.RoleApplicationInput
import com.prayatna.lookiesapp.domain.model.user.UserAddress
import com.prayatna.lookiesapp.domain.repository.UserRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.Helper
import com.prayatna.lookiesapp.utils.compressImage
import com.prayatna.lookiesapp.utils.extractSupabaseError
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val storage: Storage,
    private val userPreference: UserPreference,
    private val supabaseUserService: SupabaseUserService,
    @ApplicationContext private val context: Context,
): UserRepository {
    override suspend fun updateFcmToken(token: String): DataResult<Unit> {
        return try {
            val result = supabaseUserService.updateFcmToken(token)
            DataResult.Success(result)
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            Log.e("UserRepositoryImpl", "updateFcmToken: $msg")
            DataResult.Error(msg)
        }
    }

    override fun getProfile(): Flow<DataResult<ProfileDto>> =
        userPreference.getProfile()
            .map { localProfile ->
                if (localProfile.id == null) {
                    DataResult.Loading
                } else {
                    DataResult.Success(localProfile.toDto())
                }
            }
            .onStart {
                try {
                    val userId = auth.currentUserOrNull()?.id ?: return@onStart

                    val remoteProfile = postgrest
                        .from("users_view")
                        .select {
                            filter { eq("user_id", userId) }
                        }
                        .decodeSingle<ProfileDto>()

                    userPreference.setProfile(remoteProfile.asDomainModel())

                } catch (e: RestException) {
                    val msg = extractSupabaseError(e.error)
                    emit(DataResult.Error(msg))
                } catch (e: HttpRequestException) {
                    emit(DataResult.Error(e.message ?: "Network error"))
                } catch (e: Exception) {
                    emit(DataResult.Error("Something went wrong! Please check your connection"))
                }
            }


    override suspend fun editProfile(fullName: String, bio: String, address: String, username: String): DataResult<String> {
        return try {
            val result = supabaseUserService.editProfile(
                fullName = fullName,
                bio = bio,
                address = address,
                username = username
            )
            DataResult.Success(result)
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message ?: "Network error")
        } catch (e: Exception) {
            DataResult.Error("Something went wrong! Please check your connection")
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
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message ?: "Network error")
        } catch (e: Exception) {
            DataResult.Error("Something went wrong! Please check your connection")
        }
    }

    override suspend fun registerBusiness(
        request: RoleApplicationInput,
        kycFile: Uri,
        fileName: String
    ): DataResult<RoleApplicationResponse> {
        val compressedImage = kycFile.compressImage(context, 500_000L)
            ?: return DataResult.Error("Image is not selected")
        return try {
            val result = supabaseUserService.registerBusiness(
                request = request.toDto(),
                kycFile = compressedImage,
                fileName = fileName
            )
            if (result.status == "success") {
                DataResult.Success(result)
            } else {
                DataResult.Error(result.message)
            }
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Something went wrong! Please check your connection")
        }
    }

    override suspend fun becomeArtist(
        request: ArtistApplicationInput,
        kycFile: Uri,
        fileName: String
    ): DataResult<RoleApplicationResponse> {
        val compressedImage = kycFile.compressImage(context, 500_000L)
            ?: return DataResult.Error("Image is not selected")
        return try {
            val result = supabaseUserService.becomeArtist(
                request = request.toDto(),
                kycFile = compressedImage,
                fileName = fileName
            )
            if (result.status == "success") {
                DataResult.Success(result)
            } else {
                DataResult.Error(result.message)
            }
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Something went wrong! Please check your connection")
        }
    }

    override suspend fun getFcmToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: FirebaseException) {
            e.message ?: "Something went wrong! Please check your connection"
        }
    }

    override suspend fun getUserAddresses(): DataResult<List<UserAddress>> {
        return try {
            val result = supabaseUserService.getUserAddresses()
            DataResult.Success(result.map { it.toDomain() })
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        }
    }

    override suspend fun createUserAddress(address: CreateUserAddressInput): DataResult<UserAddress> {
        return try {
            val result = supabaseUserService.createUserAddress(address = address.toDto())
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        }
    }
}
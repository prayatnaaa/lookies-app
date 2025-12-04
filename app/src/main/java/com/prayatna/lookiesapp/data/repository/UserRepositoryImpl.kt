package com.prayatna.lookiesapp.data.repository

import android.content.Context
import android.util.Log
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseUserService
import com.prayatna.lookiesapp.data.remote.dto.ProfileDto
import com.prayatna.lookiesapp.data.mapper.asDomainModel
import com.prayatna.lookiesapp.data.mapper.toDto
import com.prayatna.lookiesapp.data.remote.request.user.PartnerApplicationRequest
import com.prayatna.lookiesapp.domain.model.user.PartnerSubmissionParams
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

    override suspend fun submitPartnerApplication(params: PartnerSubmissionParams): DataResult<String> {
        return try {
            val compressedLogo = params.partnerLogo.compressImage(context, 500_000L)
            val compressedKtp = params.ktpFile.compressImage(context, 500_000L)
            val compressedLicense = params.businessLicenseFile.compressImage(context, 500_000L)

            if (compressedLogo == null || compressedKtp == null || compressedLicense == null) {
                return DataResult.Error("Failed to generate images! make sure all images are valid")
            }

            val serviceParams = PartnerApplicationRequest(
                partnerName = params.partnerName,
                partnerType = params.partnerType,
                locName = params.locName,
                locUrl = params.locUrl,
                partnerPortfolioLink = params.partnerPortfolioLink,
                bankName = params.bankName,
                bankAccountNumber = params.bankAccountNumber,
                bankAccountHolder = params.bankAccountHolder,
                partnerLogo = compressedLogo,
                ktpFile = compressedKtp,
                businessLicenseFile = compressedLicense
            )
            val response = supabaseUserService.submitPartnerApplication(serviceParams)
            DataResult.Success(response)
        } catch (e: RestException) {
            Log.e("PartnerApplication", e.toString())
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        } catch (e: HttpRequestException) {
            Log.e("PartnerApplication", e.message.toString())
           DataResult.Error(e.message ?: "Network error")
        } catch (e: Exception) {
            Log.e("PartnerApplication", e.message.toString())
           DataResult.Error("Something went wrong! Please check your connection")
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
                        .from("user_profiles")
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

}
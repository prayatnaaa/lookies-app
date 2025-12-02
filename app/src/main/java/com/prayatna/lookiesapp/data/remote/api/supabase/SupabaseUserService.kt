package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.data.remote.dto.UserDto
import com.prayatna.lookiesapp.data.remote.request.user.PartnerApplicationRequest
import com.prayatna.lookiesapp.data.remote.response.base.RpcBaseResponse
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

class SupabaseUserService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val storage: Storage
) {
    suspend fun getUser(): UserDto {
        val userId = auth.currentUserOrNull()?.id

        if (userId.isNullOrEmpty()) {
            throw Exception("Failed to get user! user not found.")
        }

        val user = postgrest.from("user_view")
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingle<UserDto>()

        Log.d("USER", "user:$user")

        return user
    }

    suspend fun submitPartnerApplication(data: PartnerApplicationRequest): String = coroutineScope { // Gunakan coroutineScope untuk upload paralel

        val userId = auth.currentUserOrNull()?.id ?: throw IllegalStateException("User not logged in")
        // 1. Definisikan Path dan Bucket
        val logoPath = "partner-logos/${UUID.randomUUID()}.png"
        val ktpPath = "legal-docs/$userId/ktp-${UUID.randomUUID()}.jpg"
        val licensePath = "legal-docs/$userId/license-${UUID.randomUUID()}.jpg"

        val publicBucket = "partner_assets"
        val privateBucket = "private_documents"

        // 2. Upload File secara PARALEL (Supaya lebih cepat)
        val uploadLogoDeferred = async {
            storage.from(publicBucket).upload(
                path = logoPath,
                data = data.partnerLogo,
                upsert = true
            )
            // Return URL Publik untuk Logo
            Helper.buildImageUrl(imageName = logoPath, bucketName = publicBucket)
        }

        val uploadKtpDeferred = async {
            storage.from(privateBucket).upload(
                path = ktpPath,
                data = data.ktpFile,
                upsert = true,
            )
            // Return Path saja (karena private, nanti download pakai createSignedUrl)
            // Atau simpan full URL jika Helper Anda mendukung private url generation
            ktpPath
        }

        val uploadLicenseDeferred = async {
            storage.from(privateBucket).upload(
                path = licensePath,
                data = data.businessLicenseFile,
                upsert = true
            )
            licensePath
        }

        val logoUrl = uploadLogoDeferred.await()
        val ktpUrlOrPath = uploadKtpDeferred.await()
        val licenseUrlOrPath = uploadLicenseDeferred.await()

        val response = postgrest.rpc(
            function = "submit_partner_application",
            parameters = mapOf(
                "p_loc_name" to data.locName,
                "p_loc_url" to data.locUrl,
                "p_partner_name" to data.partnerName,
                "p_partner_type" to data.partnerType,
                "p_partner_logo_url" to logoUrl,
                "p_partner_portfolio_link" to data.partnerPortfolioLink,

                // Parameter for Payouts
                "p_ktp_owner_url" to ktpUrlOrPath,
                "p_business_license_url" to licenseUrlOrPath,
                "p_bank_name" to data.bankName,
                "p_bank_account_number" to data.bankAccountNumber,
                "p_bank_account_holder" to data.bankAccountHolder
            )
        )

        Log.d("PartnerSubmission", "result: $response")

        val jsonStr = response.data
        val result = Json.decodeFromString<RpcBaseResponse>(jsonStr)
        Log.d("PartnerApplication", result.message)

        return@coroutineScope result.message
    }

    suspend fun editProfile(
        fullName: String,
        bio: String,
        address: String,
        username: String
    ): String {
        val userId = auth.currentUserOrNull()?.id
            ?: throw Exception("User not authenticated")

        val result = postgrest.from("user_profiles")
            .update({
                set("full_name", fullName)
                set("bio", bio)
                set("address", address)
                set("username", username)
            }) {
                filter { eq("user_id", userId) }
            }

        return result.data
    }

    private suspend fun uploadPartnerLogo(image: ByteArray): String {
        if (image.isEmpty()) throw Exception("Image is empty")

        val path = "partner-logos/${UUID.randomUUID()}.png"
        val bucketName = "partner_assets"

        val imageUrl = storage.from(bucketName).upload(
            path = path,
            data = image,
            upsert = true
        )

        val fullPublicUrl = Helper.buildImageUrl(imageName = imageUrl, bucketName = bucketName)

        Log.d("PartnerLogo", "Logo updated: $fullPublicUrl")
        return fullPublicUrl
    }

    suspend fun editProfileImage(image: ByteArray): String {
        if (image.isEmpty()) throw Exception("Image is empty")

        val path = UUID.randomUUID().toString()
        val imageUrl = storage.from("user_profile_image").upload(
            path = "$path.png",
            data = image,
            upsert = true
        )

        val userId = auth.currentUserOrNull()?.id
            ?: throw Exception("User not authenticated")

        val updateResult = postgrest.from("user_profiles")
            .update({
                set(
                    "profile_picture_url",
                    Helper.buildImageUrl(imageName = imageUrl, bucketName = "user_profile_image")
                )
            }) {
                filter { eq("user_id", userId) }
            }

        return updateResult.data
    }
}
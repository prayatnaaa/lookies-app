package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.MerchantMemberDto
import com.prayatna.lookiesapp.data.remote.dto.UserAddressDto
import com.prayatna.lookiesapp.data.remote.dto.UserEmailDto
import com.prayatna.lookiesapp.data.remote.dto.request.user.AcceptInvitationRequest
import com.prayatna.lookiesapp.data.remote.dto.request.user.ArtistApplicationRequest
import com.prayatna.lookiesapp.data.remote.dto.request.user.CreateUserAddressRequest
import com.prayatna.lookiesapp.data.remote.dto.request.user.RoleApplicationRequest
import com.prayatna.lookiesapp.data.remote.dto.response.user.RoleApplicationResponse
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.storage.Storage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.util.UUID
import javax.inject.Inject

class SupabaseUserService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val storage: Storage,
    private val httpClient: HttpClient
) {

    suspend fun acceptPartnerInvitations(merchantAccountId: String): MerchantMemberDto {

        return postgrest.rpc(
            function = "accept_partner_invitation",
            parameters = AcceptInvitationRequest(merchantAccountId = merchantAccountId)
        ).decodeList<MerchantMemberDto>()[0]
    }

    suspend fun rejectPartnerInvitations(merchantAccountId: String): MerchantMemberDto {
        val userId = auth.currentSessionOrNull()?.user?.id ?:
        throw IllegalStateException("User not logged in")

        return postgrest["merchant_members"].update(
            {
                MerchantMemberDto::status setTo "inactive"
            }
        ) {
            select()
            filter {
                MerchantMemberDto::userId eq userId
                MerchantMemberDto::merchantAccountId eq merchantAccountId
            }
        }.decodeSingle<MerchantMemberDto>()
    }

    suspend fun getUsersEmail(query: String? = null): List<UserEmailDto> {
        return postgrest.from("users")
            .select(Columns.list("id", "email")) {
                query?.let {
                    filter {
                        ilike("email", "%$it%")
                    }
                }
            }
            .decodeList<UserEmailDto>()
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

    suspend fun registerBusiness(
        request: RoleApplicationRequest,
        kycFiles: List<Pair<String, ByteArray>> // Pair of FileName to ByteArray
    ): RoleApplicationResponse {

        val session = auth.currentSessionOrNull()
            ?: throw IllegalStateException("No active session")

        val userId = auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("User not logged in")

        val uploadedPaths = mutableListOf<String>()

        try {
            val updatedKycDocuments = request.businessPayload.kycDocuments.toMutableList()

            // Process files and update request payload
            kycFiles.forEachIndexed { index, (filename, content) ->
                // Basic cleanup for file naming
                val safeType = updatedKycDocuments[index].type.lowercase().replace("_", "-")
                val extension = filename.substringAfterLast(".", "")
                val path = "$userId/${UUID.randomUUID()}_$safeType.$extension"
                
                storage.from("private_documents").upload(
                    path = path,
                    data = content
                )
                uploadedPaths.add(path)

                if (index < updatedKycDocuments.size) {
                    updatedKycDocuments[index] = updatedKycDocuments[index].copy(
                        fileId = path
                    )
                }
            }

            val updatedBusinessPayload = request.businessPayload.copy(
                kycDocuments = updatedKycDocuments,
                userId = userId
            )

            val finalRequest = request.copy(
                businessPayload = updatedBusinessPayload
            )

            val response = httpClient
                .post("${BuildConfig.SUPABASE_EDGE_BASE_URL}/role-application") {
                    contentType(ContentType.Application.Json)
                    header("Authorization", "Bearer ${session.accessToken}")
                    setBody(finalRequest)
                }
            return response.body()

        } catch (e: Exception) {
            Log.e("Supabase", "Error uploading KYC files, rolling back...", e)
            uploadedPaths.forEach { pathToDelete ->
                try {
                    storage.from("private_documents").delete(pathToDelete)
                } catch (deleteErr: Exception) {
                    Log.e("Supabase", "Failed to rollback file $pathToDelete", deleteErr)
                }
            }
            throw e
        }
    }

    suspend fun becomeArtist(
        request: ArtistApplicationRequest,
        kycFile: ByteArray,
        fileName: String
    ): RoleApplicationResponse {

        val session = auth.currentSessionOrNull()
            ?: throw IllegalStateException("No active session")

        val userId = auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("User not logged in")

        var uploadedPath: String? = null

        try {
            val safeFileName = fileName.replace(" ", "_")
            val path = "$userId/$safeFileName"

            storage.from("private_documents").upload(
                path = path,
                data = kycFile,
//                upsert = true
            )

            uploadedPath = path
            Log.d("Supabase", "File uploaded successfully at: $path")

            val updatedKycList = request.kycDocuments?.toMutableList()

            if (updatedKycList != null) {
                updatedKycList[0] = updatedKycList[0].copy(
                    fileId = path
                )
            } else {
                throw IllegalStateException("List kycDocuments cannot be empty")
            }

            val finalRequest = request.copy(
                kycDocuments = updatedKycList,
                )

            val response = httpClient
                .post("${BuildConfig.SUPABASE_EDGE_BASE_URL}/become-artist") {
                    contentType(ContentType.Application.Json)
                    header("Authorization", "Bearer ${session.accessToken}")
                    setBody(finalRequest)
                }
            return response.body()

        } catch (e: Exception) {
            Log.e("Supabase", "Error logic, rolling back file...", e)

            uploadedPath?.let { pathToDelete ->
                try {
                    storage.from("private_documents").delete(pathToDelete)
                    Log.d("Supabase", "Rollback: File deleted $pathToDelete")
                } catch (deleteErr: Exception) {
                    Log.e("Supabase", "Failed to rollback file", deleteErr)
                }
            }
            throw e
        }
    }

    suspend fun getUserAddresses(): List<UserAddressDto> {
        val userId = auth.currentUserOrNull()?.id
            ?: throw Exception("User not authenticated")

        return postgrest.from("user_addresses").select {
            filter {
                eq("user_id", userId)
            }
        }.decodeList<UserAddressDto>()
    }

    suspend fun createUserAddress(address: CreateUserAddressRequest): UserAddressDto {
        val userId = auth.currentUserOrNull()?.id
            ?: throw Exception("User not authenticated")

        val finalRequest = address.copy(userId = userId)

        val result =  postgrest.from("user_addresses")
            .insert(finalRequest) {
                select()
            }
            .decodeSingle<UserAddressDto>()

        return result
    }

    suspend fun updateFcmToken(token: String) {
        val userId = auth.currentUserOrNull()?.id
            ?: throw Exception("User not authenticated")

        postgrest.from("user_profiles")
            .update({
                set("fcm_token", token)
            }) {
                filter {
                    eq("user_id", userId)
                }
            }

        Log.d("FCM-UPDATE", "Token updated")
    }
}
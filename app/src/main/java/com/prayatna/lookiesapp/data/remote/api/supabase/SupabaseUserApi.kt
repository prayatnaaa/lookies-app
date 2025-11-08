package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.data.remote.dto.UserDto
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import java.util.UUID
import javax.inject.Inject

class SupabaseUserApi @Inject constructor(
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

    suspend fun updateRoleToPartner() {}

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
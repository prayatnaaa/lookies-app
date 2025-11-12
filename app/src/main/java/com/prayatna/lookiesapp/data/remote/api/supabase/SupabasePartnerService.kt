package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import java.util.UUID
import javax.inject.Inject

class SupabasePartnerService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val storage: Storage
) {
    private suspend fun uploadPartnerLogo(image: ByteArray): String {
        if (image.isEmpty()) throw Exception("Image is empty")

        val userId = auth.currentUserOrNull()?.id
            ?: throw Exception("User not authenticated")

        val path = "partner-logos/${UUID.randomUUID()}.png"
        val bucketName = "partner_assets"

        val imageUrl = storage.from(bucketName).upload(
            path = path,
            data = image,
            upsert = true
        )

        val fullPublicUrl = Helper.buildImageUrl(imageName = imageUrl, bucketName = bucketName)

        val updateResult = postgrest.from("partner_profiles")
            .update({
                set("logo_url", fullPublicUrl)
            }) {
                filter { eq("profile_id", userId) }
            }

        Log.d("PartnerLogo", "Logo updated: ${updateResult.data}")
        return fullPublicUrl
    }

    suspend fun getPartnerProfile() {
        val userId = auth.currentUserOrNull()?.id
            ?: throw Exception("User not authenticated")
        val partnerProfile = postgrest.from("partner_profiles")
            .select {
                filter {
                    eq("profile_id", userId)
                }
            }
    }
}
package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.DetailPartnerDto
import com.prayatna.lookiesapp.data.remote.dto.PartnerDto
import com.prayatna.lookiesapp.utils.Helper
import com.prayatna.lookiesapp.utils.JsonProvider
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.Storage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import java.util.UUID
import javax.inject.Inject

class SupabasePartnerService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val storage: Storage,
    private val httpClient: HttpClient
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

    suspend fun getPartners(): List<PartnerDto> {
        val result = postgrest
            .from("partner_profiles").select(
                columns = Columns.list("user_id", "name", "logo_url", "status")
            )
            .decodeList<PartnerDto>()
        return result
    }

    suspend fun getDetailPartner(id: String): DetailPartnerDto {
        Log.d("PartnerRepository", "Getting detail partner with id: $id")
        val response: HttpResponse = httpClient
            .get("${BuildConfig.SUPABASE_EDGE_BASE_URL}/get-detail-partner?id=${id}") {
                auth.currentSessionOrNull()?.let {
                    header("Authorization", "Bearer ${it.accessToken}")
                }
            }
        if (response.status != HttpStatusCode.OK) {
            throw Exception("Failed! ${response.status}")
        }
        return JsonProvider.json.decodeFromString(response.body())
    }

    suspend fun updatePartner() {}

}
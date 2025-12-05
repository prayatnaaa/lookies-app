package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.response.painting.GetPaintingDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SupabasePaintingService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val storage: Storage,
) {

//    fun getPaintings(): Flow<List<GetPaintingDto>> {
//        return postgrest.from("paintings") {
//
//        }
//    }
    suspend fun getPaintingById(): GetPaintingDto{
        val artistId = auth.currentUserOrNull()?.id ?: throw IllegalStateException("User not logged in")
        val response = postgrest.from("paintings").select {
            filter {
                eq("artist_id", artistId)
            }
        }.decodeSingle<GetPaintingDto>()

        return response
    }

}
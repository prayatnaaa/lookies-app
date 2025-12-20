package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseArtistService
import com.prayatna.lookiesapp.domain.repository.ArtistRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val supabaseArtistService: SupabaseArtistService,
    private val auth: Auth
): ArtistRepository {
    override suspend fun registerEvent(
        eventId: Int,
        paintingIds: List<Int>
    ): DataResult<String> {
        val artistId = auth.currentSessionOrNull()?.user?.id ?: throw Exception("User not logged in")
        return try {
            val response = supabaseArtistService.registerEvent(
                artistId = artistId,
                eventId = eventId,
                paintingIds = paintingIds
            )
            Log.d("RegisterEvent", response)
            DataResult.Success(response)
        } catch (e: RestException) {
            Log.e("RegisterEvent", e.toString())
            DataResult.Error(e.error)
        }
    }
}
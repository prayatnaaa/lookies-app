package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.LocationDto
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class SupabaseLocationService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
) {

    suspend fun getLocationsById(): List<LocationDto> {
        val userId = auth.currentUserOrNull()?.id ?: throw Exception("User not authenticated")
        val locations = postgrest.from("locations")
            .select {
                filter {
                    eq("user_id", userId)
                }
            }.decodeList<LocationDto>()

        return locations
    }

    suspend fun addLocation(name: String, url: String): LocationDto {
        val userId = auth.currentUserOrNull()?.id ?: throw Exception("User not authenticated")
        val location = LocationDto(userId = userId, name = name, url = url)
        val response = postgrest.from("locations")
            .insert(location) {
                select()
            }.decodeSingle<LocationDto>()
        return response
    }
}
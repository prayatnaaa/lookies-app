package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseLocationService
import com.prayatna.lookiesapp.domain.model.location.Location
import com.prayatna.lookiesapp.domain.repository.LocationRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val supabaseLocationService: SupabaseLocationService
): LocationRepository {
    override suspend fun getLocationsById(): DataResult<List<Location>> {
        return try {
            val response = supabaseLocationService.getLocationsById()
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message ?: "Network error")
        } catch (e: Exception) {
            DataResult.Error("Something went wrong! Please check your connection")
        }
    }

    override suspend fun addLocation(name: String, url: String): DataResult<Location> {
        return try {
            if (name.isEmpty() || url.isEmpty()) {
                return DataResult.Error("Please complete the form")
            }

            val response = supabaseLocationService.addLocation(
                name = name,
                url = url
            )
            DataResult.Success(response.toDomain())
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
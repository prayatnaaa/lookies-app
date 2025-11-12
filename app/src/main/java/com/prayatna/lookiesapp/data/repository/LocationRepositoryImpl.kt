package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.mapper.asDomainModel
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseLocationService
import com.prayatna.lookiesapp.domain.model.location.Location
import com.prayatna.lookiesapp.domain.repository.LocationRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val supabaseLocationService: SupabaseLocationService
): LocationRepository {
    override suspend fun getLocationsById(): DataResult<List<Location>> {
        return try {
            val response = supabaseLocationService.getLocationsById()
            DataResult.Success(response.map { it.asDomainModel() })
        } catch (e: RestException) {
            DataResult.Error(e.message.toString())
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message.toString())
        } catch (e: Exception) {
            DataResult.Error(e.message.toString())
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
            DataResult.Success(response.asDomainModel())
        } catch (e: RestException) {
            DataResult.Error(e.message.toString())
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message.toString())
        } catch (e: Exception) {
            DataResult.Error(e.message.toString())
        }
    }
}
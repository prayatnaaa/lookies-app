package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.mapper.toDto
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabasePartnerService
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.model.EventParticipant
import com.prayatna.lookiesapp.domain.model.event.EditEventInput
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.partner.DetailPartner
import com.prayatna.lookiesapp.domain.model.partner.Partner
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PartnerRepositoryImpl @Inject constructor(
    private val supabasePartnerService: SupabasePartnerService
): PartnerRepository {
    override fun getPartners(): Flow<DataResult<List<Partner>>> = flow {
        emit(DataResult.Loading)
        try {
            val response = supabasePartnerService.getPartners()
            emit(DataResult.Success(response.map { it.toDomain() }))
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            emit(DataResult.Error(msg))
        } catch (e: HttpRequestException) {
            emit(DataResult.Error(e.message ?: "Network error"))
        } catch (e: Exception) {
            Log.e("PartnerList", e.toString())
            emit(DataResult.Error("Something went wrong! Please check your connection"))
        }
    }

    override suspend fun getDetailPartner(id: String): DataResult<DetailPartner> {
        return try {
            val response = supabasePartnerService.getDetailPartner(id)
            Log.d("PartnerRepository", response.toString())
            DataResult.Success(response.toDomain()!!)
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message ?: "Network error")
        } catch (e: Exception) {
            Log.e("PartnerRepository", e.toString())
            DataResult.Error("Something went wrong! Please check your connection")
        }
    }

    override suspend fun getSelfEvents(): DataResult<List<Event>> {
        return try {
            val response = supabasePartnerService.getSelfEvents()
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        }
    }

    override suspend fun updateEvent(id: String, input: EditEventInput): DataResult<Event> {
        return try {
            val response = supabasePartnerService.updateEvent(id = id, request = input.toDto())
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            DataResult.Error(e.error)
        }
    }

    override suspend fun getParticipantList(eventId: String?): DataResult<List<EventParticipant>> {
        return try {
            val response = supabasePartnerService.getParticipantList(eventId)
            if (response.isEmpty()) {
                return DataResult.Error("No participant found")
            }
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        }
    }
}
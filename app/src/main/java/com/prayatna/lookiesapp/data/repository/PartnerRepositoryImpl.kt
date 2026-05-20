package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.mapper.toDto
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabasePartnerService
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.model.EventParticipant
import com.prayatna.lookiesapp.domain.model.event.DefaultEvent
import com.prayatna.lookiesapp.domain.model.event.EditEventInput
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBusiness
import com.prayatna.lookiesapp.domain.model.merchant.MerchantDetail
import com.prayatna.lookiesapp.domain.model.painting.InsertSelfEventPaintingsResult
import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.domain.model.partner.PartnerDashboard
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import com.prayatna.lookiesapp.utils.mapUpdateErrorToMessage
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PartnerRepositoryImpl @Inject constructor(
    private val supabasePartnerService: SupabasePartnerService
): PartnerRepository {
    override fun getPartners(
        status: String?,
        name: String?,
        kycStatus: String?,
        merchantType: String?
    ): Flow<DataResult<List<MerchantBusiness>>> = flow {
        emit(DataResult.Loading)
        try {
            val response = supabasePartnerService.getPartners(
                status = status,
                name = name,
                kycStatus = kycStatus,
                merchantType = merchantType
            )
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

    override suspend fun getDetailPartner(id: String): DataResult<MerchantDetail> {
        return try {
            val response = supabasePartnerService.getDetailPartner(id)
            DataResult.Success(response.toDomain())
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

    override suspend fun getSelfEvents(
        businessId: String,
        status: String?,
        name: String?
    ): DataResult<List<Event>> {
        return try {
            val response = supabasePartnerService.getSelfEvents(
                status = status,
                name = name,
                businessId = businessId
            )
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        }
    }

    override suspend fun updateEvent(id: String, input: EditEventInput): DataResult<DefaultEvent> {
        return try {
            val response = supabasePartnerService.updateEvent(id = id, request = input.toDto())
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            Log.e("UpdateEvent", e.toString())

            DataResult.Error(mapUpdateErrorToMessage(e))
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

    override suspend fun approvePainting(id: String): DataResult<String> {
        return try {
            val response = supabasePartnerService.approvePainting(eventPaintingId = id)
            DataResult.Success(response)
        } catch (e: RestException) {
            DataResult.Error(e.error)
        }
    }

    override suspend fun rejectPainting(id: String, reason: String): DataResult<String> {
        return try {
            val response = supabasePartnerService.rejectPainting(eventPaintingId = id, reason = reason)
            DataResult.Success(response)
        } catch (e: RestException) {
            DataResult.Error(e.message.toString())
        }
    }

    override fun getDashboardSummary(merchantId: String): Flow<PartnerDashboard> =
        supabasePartnerService.getDashboardSummary(merchantId)
            .map { it.toDomain() }

    override suspend fun insertSelfEventPaintings(
        eventId: Int,
        selectedPaintings: List<Painting>
    ): DataResult<InsertSelfEventPaintingsResult> {
        return try {
            val result = supabasePartnerService.insertSelfEventPaintings(
                eventId = eventId.toString(),
                selectedPaintings = selectedPaintings.map { it.toDto() }
            )
            Log.d("InsertSelfEventPaintings", result.toString())
            DataResult.Success(result.first().toDomain())
        } catch (e: RestException) {
            Log.e("InsertSelfEventPaintings", e.toString())
            DataResult.Error(e.error)
        }
    }
}
package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseAdminService
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.model.admin.DecideEventResult
import com.prayatna.lookiesapp.domain.model.admin.DecidePartnerApplicationResult
import com.prayatna.lookiesapp.domain.repository.AdminRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.RestException
import javax.inject.Inject

class AdminRepositoryImpl @Inject constructor(
    private val supabaseAdminService: SupabaseAdminService
): AdminRepository {

    private suspend fun decidePartner(
        status: String,
        id: String
    ): DataResult<DecidePartnerApplicationResult> {
        return try {
            val response = supabaseAdminService.decidePartnerApplication(status, id)
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        }
    }

    private suspend fun decideEvent(
        status: String,
        id: String
    ): DataResult<DecideEventResult> {
        return try {
            val result = supabaseAdminService.decideEvent(status, id)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        }
    }

    override suspend fun approvePartner(partnerId: String): DataResult<DecidePartnerApplicationResult> =
        decidePartner("approved", partnerId)

    override suspend fun rejectPartner(partnerId: String): DataResult<DecidePartnerApplicationResult> =
        decidePartner("rejected", partnerId)

    override suspend fun approveEvent(eventId: String): DataResult<DecideEventResult> =
        decideEvent("published", eventId)


    override suspend fun rejectEvent(eventId: String): DataResult<DecideEventResult> =
        decideEvent("rejected", eventId)
}

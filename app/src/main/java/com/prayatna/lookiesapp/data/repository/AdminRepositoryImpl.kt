package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseAdminService
import com.prayatna.lookiesapp.domain.model.partner.Partner
import com.prayatna.lookiesapp.domain.repository.AdminRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.RestException
import javax.inject.Inject

class AdminRepositoryImpl @Inject constructor(
    private val supabaseAdminService: SupabaseAdminService
): AdminRepository {

    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "Something went wrong"
    }

    override suspend fun getPendingPartners(): DataResult<List<Partner>> {
        TODO("Not yet implemented")
    }

    private suspend fun decidePartner(
        status: String,
        id: Long
    ): DataResult<String> {
        return try {
            val response = supabaseAdminService.decidePartnerApplication(status, id)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: DEFAULT_ERROR_MESSAGE)
        } catch (e: RestException) {
            DataResult.Error(e.message ?: DEFAULT_ERROR_MESSAGE)
        }
    }

    override suspend fun approvePartner(partnerId: Long): DataResult<String> =
        decidePartner("approved", partnerId)

    override suspend fun rejectPartner(partnerId: Long): DataResult<String> =
        decidePartner("disapproved", partnerId)
}

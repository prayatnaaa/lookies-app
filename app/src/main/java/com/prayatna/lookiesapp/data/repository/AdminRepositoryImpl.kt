package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseAdminService
import com.prayatna.lookiesapp.domain.model.partner.Partner
import com.prayatna.lookiesapp.domain.repository.AdminRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.HttpRequestException
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
        id: String
    ): DataResult<String> {
        return try {
            val response = supabaseAdminService.decidePartnerApplication(status, id)
            DataResult.Success(response)
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message ?: "Network error")
        } catch (e: Exception) {
            DataResult.Error("Something went wrong! Please check your connection")
        }
    }

    override suspend fun approvePartner(partnerId: String): DataResult<String> =
        decidePartner("approved", partnerId)

    override suspend fun rejectPartner(partnerId: String): DataResult<String> =
        decidePartner("disapproved", partnerId)
}

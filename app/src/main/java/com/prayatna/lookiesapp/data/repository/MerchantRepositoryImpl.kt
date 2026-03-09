package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseMerchantService
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.model.merchant.MerchantMember
import com.prayatna.lookiesapp.domain.model.merchant.MerchantProfile
import com.prayatna.lookiesapp.domain.repository.MerchantRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.RestException
import javax.inject.Inject

class MerchantRepositoryImpl @Inject constructor(
    private val supabaseMerchantService: SupabaseMerchantService
): MerchantRepository {
    override suspend fun getMerchantProfile(businessId: String): DataResult<MerchantProfile> {
        return try {
            val response = supabaseMerchantService.getMerchantProfile(id = businessId)
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun getMerchantMembers(userId: String?): DataResult<List<MerchantMember>> {
        return try {
            val response = supabaseMerchantService.getMerchantMembers(userId)
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }
}
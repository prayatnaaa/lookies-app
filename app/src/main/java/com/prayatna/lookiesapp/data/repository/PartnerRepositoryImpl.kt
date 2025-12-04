package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabasePartnerService
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
}
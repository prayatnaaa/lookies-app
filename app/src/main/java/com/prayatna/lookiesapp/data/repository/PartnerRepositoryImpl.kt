package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabasePartnerService
import com.prayatna.lookiesapp.domain.model.partner.DetailPartner
import com.prayatna.lookiesapp.domain.model.partner.Partner
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.utils.DataResult
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
            emit(DataResult.Error(e.message.toString()))
        } catch (e: Exception) {
            emit(DataResult.Error(e.message.toString()))
        }
    }


    override suspend fun getDetailPartner(id: Int): DataResult<DetailPartner> {
        return try {
            val response = supabasePartnerService.getDetailPartner(id)
            Log.d("PartnerRepository", response.toString())
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            Log.e("PartnerRepository", e.message.toString())
            DataResult.Error(e.message.toString())
        } catch (e: Exception) {
            Log.e("PartnerRepository", e.message.toString())
            DataResult.Error(e.message.toString())
        }
    }
}
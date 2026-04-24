package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseShipmentService
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.model.shipment.Shipment
import com.prayatna.lookiesapp.domain.model.shipment.ShipmentFee
import com.prayatna.lookiesapp.domain.repository.ShipmentRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.RestException
import javax.inject.Inject

class ShipmentRepositoryImpl @Inject constructor(
    private val supabaseShipmentService: SupabaseShipmentService
): ShipmentRepository {
    override suspend fun getShipmentByOrderId(orderId: String): DataResult<Shipment> {
        return try {
            val result = supabaseShipmentService.getShipmentByOrderId(orderId)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun getShipmentFees(): DataResult<List<ShipmentFee>> {
        return try {
            val result = supabaseShipmentService.getShipmentFees()
            DataResult.Success(result.map { it.toDomain() })
        }catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }
}
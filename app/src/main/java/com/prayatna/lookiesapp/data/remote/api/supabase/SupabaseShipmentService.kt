package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.ShipmentDto
import com.prayatna.lookiesapp.data.remote.dto.ShipmentFeeDto
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class SupabaseShipmentService @Inject constructor(
    private val postgrest: Postgrest
) {

    suspend fun getShipmentByOrderId(orderId: String): ShipmentDto {
        return postgrest.from("shipments").select {
            filter {
                eq("order_id", orderId)
            }
        }.decodeSingle<ShipmentDto>()
    }

    suspend fun getShipmentFees(): List<ShipmentFeeDto> {
        return postgrest.from("shipment_fees").select().decodeList<ShipmentFeeDto>()
    }

}
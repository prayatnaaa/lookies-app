package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.data.remote.dto.ExhibitionShipmentDto
import com.prayatna.lookiesapp.data.remote.dto.ShipmentDto
import com.prayatna.lookiesapp.data.remote.dto.ShipmentFeeDto
import com.prayatna.lookiesapp.data.remote.dto.request.shipment.CreateExhibitionShipmentRequest
import com.prayatna.lookiesapp.domain.mapper.toInsertDto
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


    suspend fun createExhibitionShipment(data: CreateExhibitionShipmentRequest): ExhibitionShipmentDto {
        return postgrest.from("exhibition_shipments")
            .insert(data) {
                select()
            }
            .decodeSingle<ExhibitionShipmentDto>()
    }

    suspend fun updateExhibitionShipmentStatus(shipmentId: String, notes: String?, status: String): ExhibitionShipmentDto {
        return postgrest.from("exhibition_shipments")
            .update({
                if (notes != null) {
                    set("notes", notes)
                }
                set("status", status)
            }) {
                select()
                filter {
                    eq("id", shipmentId)

                }
            }.decodeSingle<ExhibitionShipmentDto>()
    }

    suspend fun getExhibitionShipmentByEventPaintingId(eventPaintingId: String): ExhibitionShipmentDto? {
        val response = postgrest.from("exhibition_shipments").select {
            filter {
                eq("event_painting_id", eventPaintingId)
            }
        }.decodeSingleOrNull<ExhibitionShipmentDto>()
        Log.d("SupabaseShipmentService", "getExhibitionShipmentByEventPaintingId: $response")
        return response
    }

}
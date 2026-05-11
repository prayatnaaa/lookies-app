package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.data.remote.dto.ExhibitionShipmentDto
import com.prayatna.lookiesapp.data.remote.dto.ShipmentDto
import com.prayatna.lookiesapp.data.remote.dto.ShipmentFeeDto
import com.prayatna.lookiesapp.data.remote.dto.request.shipment.CreateExhibitionShipmentRequest
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

class SupabaseShipmentService @Inject constructor(
    private val postgrest: Postgrest,
    private val storage: Storage
) {

    suspend fun uploadArrivalProof(shipmentId: String, image: ByteArray): String {
        val fileName = "${UUID.randomUUID()}.png"
        val bucketName = "shipment_proofs"
        val path = "exhibition/$fileName"

        val uploadedPath = storage.from(bucketName).upload(
            path = path,
            data = image,
            upsert = true
        )

        val imageUrl = Helper.buildImageUrl(imageName = uploadedPath, bucketName = bucketName)

        postgrest.from("exhibition_shipments").update({
            set("arrival_proof_url", imageUrl)
        }) {
            filter { eq("id", shipmentId) }
        }

        return imageUrl
    }

    suspend fun getShipmentByOrderId(orderId: String): ShipmentDto {
        return postgrest.from("shipments").select {
            filter {
                eq("order_id", orderId)
                neq("status", "awaiting_payment")
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
                if (status == "delivered") {
                    set("delivered_at", Clock.System.now())
                }
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
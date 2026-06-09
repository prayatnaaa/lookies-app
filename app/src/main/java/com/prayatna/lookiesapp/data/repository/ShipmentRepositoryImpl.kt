package com.prayatna.lookiesapp.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import coil.network.HttpException
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseShipmentService
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.shipment.Shipment
import com.prayatna.lookiesapp.domain.model.shipment.ShipmentFee
import com.prayatna.lookiesapp.domain.repository.ShipmentRepository
import com.prayatna.lookiesapp.domain.model.shipment.CreateExhibitionShipmentInput
import com.prayatna.lookiesapp.domain.model.shipment.ExhibitionShipment
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.compressImage
import com.prayatna.lookiesapp.utils.extractSupabaseError
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.exceptions.RestException
import javax.inject.Inject

class ShipmentRepositoryImpl @Inject constructor(
    private val supabaseShipmentService: SupabaseShipmentService,
    @param:ApplicationContext private val context: Context
): ShipmentRepository {
    override suspend fun getShipmentByOrderId(orderId: String): DataResult<Shipment> {
        return try {
            val result = supabaseShipmentService.getShipmentByOrderId(orderId)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getShipmentFees(): DataResult<List<ShipmentFee>> {
        return try {
            val result = supabaseShipmentService.getShipmentFees()
            DataResult.Success(result.map { it.toDomain() })
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun createExhibitionShipment(input: CreateExhibitionShipmentInput): DataResult<ExhibitionShipment> {
        return try {
            val result = supabaseShipmentService.createExhibitionShipment(input.toDto())
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun updateExhibitionShipmentStatus(
        shipmentId: String,
        notes: String?,
        status: String
    ): DataResult<ExhibitionShipment> {
        return try {
            val result = supabaseShipmentService.updateExhibitionShipmentStatus(shipmentId, notes, status)
            Log.d("ShipmentRepositoryImpl", "updateExhibitionShipmentStatus: $result")
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            Log.e("ShipmentRepositoryImpl", "updateExhibitionShipmentStatus: ${e.error}")
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getExhibitionShipmentByEventPaintingId(eventPaintingId: String): DataResult<ExhibitionShipment?> {
        return try {
            val result = supabaseShipmentService.getExhibitionShipmentByEventPaintingId(eventPaintingId)
            Log.d("ShipmentRepositoryImpl", "getExhibitionShipmentByEventPaintingId: $result")
            DataResult.Success(result?.toDomain())
        } catch (e: RestException) {
            Log.e("ShipmentRepositoryImpl", "getExhibitionShipmentByEventPaintingId: ${e.error}")
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun uploadExhibitionArrivalProof(shipmentId: String, image: Uri): DataResult<String> {
        return try {
            val compressedBytes = image.compressImage(context, 500_000L)
                ?: return DataResult.Error("Failed to compress image")
            val result = supabaseShipmentService.uploadArrivalProof(shipmentId, compressedBytes)
            DataResult.Success(result)
        } catch (e: RestException) {
            val eMsg = extractSupabaseError(e.error)
            DataResult.Error(eMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Something went wrong")
        }
    }
}
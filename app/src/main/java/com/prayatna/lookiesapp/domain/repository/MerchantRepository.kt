package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.merchant.MerchantMember
import com.prayatna.lookiesapp.domain.model.merchant.MerchantProfile
import com.prayatna.lookiesapp.domain.model.transaction.Shipment
import com.prayatna.lookiesapp.utils.DataResult

interface MerchantRepository {
    suspend fun getMerchantProfile(businessId: String) : DataResult<MerchantProfile>
    suspend fun getMerchantMembers(userId: String? = null): DataResult<List<MerchantMember>>
    suspend fun updateShipmentStatus(shipmentId: String, status: String): DataResult<Shipment>
    suspend fun createTrackingNumberShipment(shipmentId: String, trackingNumber: String): DataResult<Shipment>
}
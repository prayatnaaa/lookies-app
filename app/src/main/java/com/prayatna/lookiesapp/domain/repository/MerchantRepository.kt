package com.prayatna.lookiesapp.domain.repository

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.merchant.InviteMerchantMemberInput
import com.prayatna.lookiesapp.domain.model.merchant.InviteMerchantMemberOutput
import com.prayatna.lookiesapp.domain.model.merchant.MerchantMember
import com.prayatna.lookiesapp.domain.model.merchant.MerchantProfile
import com.prayatna.lookiesapp.domain.model.shipment.Shipment
import com.prayatna.lookiesapp.utils.DataResult

interface MerchantRepository {

    suspend fun inviteMerchantMember(input: InviteMerchantMemberInput):
            DataResult<InviteMerchantMemberOutput>
    suspend fun getMerchantMembersByMerchantId(merchantBusinessId: String) :
            DataResult<List<MerchantMember>>
    suspend fun getMerchantProfile(businessId: String) :
            DataResult<MerchantProfile>
    suspend fun getMerchantMembers(userId: String? = null):
            DataResult<List<MerchantMember>>
    suspend fun updateShipmentStatus(shipmentId: String, status: String):
            DataResult<Shipment>
    suspend fun createTrackingNumberShipment(shipmentId: String, trackingNumber: String):
            DataResult<Shipment>
    suspend fun getShipmentsByMerchantId(merchantId: String):
            DataResult<List<Shipment>>
    suspend fun uploadShipmentArrivalProof(shipmentId: String, image: Uri):
            DataResult<String>
}
package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.EventParticipant
import com.prayatna.lookiesapp.domain.model.event.DefaultEvent
import com.prayatna.lookiesapp.domain.model.event.EditEventInput
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.partner.PartnerDashboard
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBusiness
import com.prayatna.lookiesapp.domain.model.merchant.MerchantDetail
import com.prayatna.lookiesapp.domain.model.painting.InsertSelfEventPaintingsResult
import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface PartnerRepository {
    fun getPartners(
        status: String? = null,
        name: String? = null,
        kycStatus: String? = null,
        merchantType: String? = null
    ): Flow<DataResult<List<MerchantBusiness>>>
    suspend fun getDetailPartner(id: String): DataResult<MerchantDetail>
    suspend fun getSelfEvents(
        businessId: String,
        status: String? = null,
        name: String? = null
    ): DataResult<List<Event>>
    suspend fun updateEvent(id: String, input: EditEventInput): DataResult<DefaultEvent>
    suspend fun getParticipantList(eventId: String?): DataResult<List<EventParticipant>>
    suspend fun approvePainting(id: String): DataResult<String>
    suspend fun rejectPainting(id: String): DataResult<String>
    fun getDashboardSummary(merchantId: String): Flow<PartnerDashboard>
    suspend fun insertSelfEventPaintings(eventId: Int, selectedPaintings: List<Painting>):
            DataResult<InsertSelfEventPaintingsResult>
}
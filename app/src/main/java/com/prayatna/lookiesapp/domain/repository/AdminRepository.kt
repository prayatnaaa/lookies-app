package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.admin.DecideEventResult
import com.prayatna.lookiesapp.domain.model.admin.DecidePartnerApplicationResult
import com.prayatna.lookiesapp.domain.model.admin.GetKycDocument
import com.prayatna.lookiesapp.domain.model.ticket.Ticket
import com.prayatna.lookiesapp.utils.DataResult

interface AdminRepository {
    suspend fun approvePartner(partnerId: String): DataResult<DecidePartnerApplicationResult>
    suspend fun rejectPartner(partnerId: String): DataResult<DecidePartnerApplicationResult>
    suspend fun approveEvent(eventId: Int): DataResult<DecideEventResult>
    suspend fun rejectEvent(eventId: Int, rejectReason: String): DataResult<DecideEventResult>
    suspend fun getKycDocuments(businessId: String): DataResult<List<GetKycDocument>>
    suspend fun getTicketByCode(code: String): DataResult<Ticket>
}
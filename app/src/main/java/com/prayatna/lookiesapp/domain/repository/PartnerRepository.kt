package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.event.EditEventInput
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.partner.DetailPartner
import com.prayatna.lookiesapp.domain.model.partner.Partner
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface PartnerRepository {
    fun getPartners(): Flow<DataResult<List<Partner>>>
    suspend fun getDetailPartner(id: String): DataResult<DetailPartner>
    suspend fun getSelfEvents(): DataResult<List<Event>>
    suspend fun updateEvent(id: String, input: EditEventInput): DataResult<Event>
}
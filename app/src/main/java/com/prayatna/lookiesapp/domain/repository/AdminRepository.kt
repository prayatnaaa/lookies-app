package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.partner.Partner
import com.prayatna.lookiesapp.utils.DataResult

interface AdminRepository {
    suspend fun getPendingPartners(): DataResult<List<Partner>>
    suspend fun approvePartner(partnerId: Long): DataResult<String>
    suspend fun rejectPartner(partnerId: Long): DataResult<String>
}
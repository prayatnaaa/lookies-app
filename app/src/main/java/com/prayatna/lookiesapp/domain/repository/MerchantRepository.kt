package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.merchant.MerchantMember
import com.prayatna.lookiesapp.domain.model.merchant.MerchantProfile
import com.prayatna.lookiesapp.utils.DataResult

interface MerchantRepository {
    suspend fun getMerchantProfile(businessId: String) : DataResult<MerchantProfile>
    suspend fun getMerchantMembers(userId: String? = null): DataResult<List<MerchantMember>>
}
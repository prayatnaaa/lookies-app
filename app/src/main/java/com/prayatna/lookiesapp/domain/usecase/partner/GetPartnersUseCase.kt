package com.prayatna.lookiesapp.domain.usecase.partner

import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBusiness
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetPartnersUseCase @Inject constructor(
    private val partnerRepository: PartnerRepository,
    private val userPref: UserPreference
) {

    operator fun invoke(
        merchantType: String? = null,
        status: String?,
        name: String?,
        kycStatus: String?,
        ): Flow<DataResult<List<MerchantBusiness>>> {
        return combine(
            userPref.getRole(),
            partnerRepository.getPartners(
                merchantType = merchantType,
                status = status,
                name = name,
                kycStatus = kycStatus
            )
        ) { role, result ->
            result.map { partners ->
                if (role == "admin") partners
                else partners
                    .filter { it.status == "approve" }
                    .filter { it.merchantType == merchantType }
            }
        }
    }
}


package com.prayatna.lookiesapp.domain.usecase.admin

import com.prayatna.lookiesapp.domain.model.admin.DecidePartnerApplicationResult
import com.prayatna.lookiesapp.domain.repository.AdminRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class RejectPartnerUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(partnerId: String): DataResult<DecidePartnerApplicationResult> {
        return adminRepository.rejectPartner(partnerId = partnerId)
    }
}
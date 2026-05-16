package com.prayatna.lookiesapp.domain.usecase.admin

import com.prayatna.lookiesapp.domain.model.withdrawal.WithdrawalRequest
import com.prayatna.lookiesapp.domain.repository.AdminRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class UpdateWithdrawalStatusUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(
        id: String,
        status: String,
        adminNotes: String? = null
    ): DataResult<WithdrawalRequest> {
        return adminRepository.updateWithdrawalStatus(id, status, adminNotes)
    }
}

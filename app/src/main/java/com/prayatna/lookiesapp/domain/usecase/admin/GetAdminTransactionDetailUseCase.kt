package com.prayatna.lookiesapp.domain.usecase.admin

import com.prayatna.lookiesapp.domain.model.admin.AdminTransactionDetail
import com.prayatna.lookiesapp.domain.repository.AdminRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetAdminTransactionDetailUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {

    suspend operator fun invoke(orderId: String): DataResult<AdminTransactionDetail> {
        return adminRepository.getTransactionDetail(orderId)
    }
}
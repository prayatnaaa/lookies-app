package com.prayatna.lookiesapp.domain.usecase.admin

import com.prayatna.lookiesapp.domain.model.admin.AdminTransaction
import com.prayatna.lookiesapp.domain.repository.AdminRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetAdminTransactionListUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(
        limit: Int,
        offset: Int,
        status: String?
    ): DataResult<List<AdminTransaction>> {
        return adminRepository.getTransactionList(
            limit = limit,
            offset = offset,
            status = status
        )
    }
}
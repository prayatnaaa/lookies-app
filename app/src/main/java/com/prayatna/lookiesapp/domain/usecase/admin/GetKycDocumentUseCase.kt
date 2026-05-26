package com.prayatna.lookiesapp.domain.usecase.admin

import com.prayatna.lookiesapp.domain.model.admin.GetKycDocument
import com.prayatna.lookiesapp.domain.repository.AdminRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetKycDocumentUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(businessId: String): DataResult<List<GetKycDocument>> {
        return adminRepository.getKycDocuments(businessId)
    }
}
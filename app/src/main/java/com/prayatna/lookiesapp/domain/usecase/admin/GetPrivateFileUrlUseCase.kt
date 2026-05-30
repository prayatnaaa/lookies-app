package com.prayatna.lookiesapp.domain.usecase.admin

import com.prayatna.lookiesapp.domain.repository.AdminRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetPrivateFileUrlUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(filePath: String): DataResult<String> {
        return adminRepository.getPrivateFileUrl(filePath)
    }
}

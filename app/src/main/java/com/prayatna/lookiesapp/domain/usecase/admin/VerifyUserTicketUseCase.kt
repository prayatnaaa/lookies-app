package com.prayatna.lookiesapp.domain.usecase.admin

import com.prayatna.lookiesapp.domain.repository.AdminRepository
import javax.inject.Inject

class VerifyUserTicketUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(code: String) = adminRepository.getTicketByCode(code)
}
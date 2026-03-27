package com.prayatna.lookiesapp.domain.usecase.admin

import com.prayatna.lookiesapp.domain.model.admin.DecideEventResult
import com.prayatna.lookiesapp.domain.repository.AdminRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class ApproveEventUseCase @Inject constructor( private val adminRepository: AdminRepository) {
    suspend operator fun invoke(eventId: Int): DataResult<DecideEventResult> {
        return adminRepository.approveEvent(eventId)
    }
}
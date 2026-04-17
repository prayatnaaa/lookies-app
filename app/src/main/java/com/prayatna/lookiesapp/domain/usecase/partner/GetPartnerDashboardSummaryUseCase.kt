package com.prayatna.lookiesapp.domain.usecase.partner

import com.prayatna.lookiesapp.domain.model.partner.PartnerDashboard
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPartnerDashboardSummaryUseCase @Inject constructor(
    private val repository: PartnerRepository
) {
    operator fun invoke(): Flow<PartnerDashboard> {
        return repository.getDashboardSummary()
    }
}

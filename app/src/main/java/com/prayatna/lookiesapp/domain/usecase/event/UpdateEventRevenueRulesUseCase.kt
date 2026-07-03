package com.prayatna.lookiesapp.domain.usecase.event

import com.prayatna.lookiesapp.domain.model.event.EventRevenueRules
import com.prayatna.lookiesapp.domain.model.event.UpdateRevenueRulesInput
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class UpdateEventRevenueRulesUseCase @Inject constructor(
    private val partnerRepository: PartnerRepository
) {
    suspend operator fun invoke(id: String, input: UpdateRevenueRulesInput): DataResult<EventRevenueRules> {
        val total = input.artistPercent + input.eventPercent + input.platformPercent
        if (total != 100) {
            return DataResult.Error("Total percentage for ${input.itemType} must be 100%")
        }

        return partnerRepository.updateRevenueRules(id, input)
    }
}

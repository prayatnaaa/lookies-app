package com.prayatna.lookiesapp.domain.usecase.user

import com.prayatna.lookiesapp.domain.model.user.PartnerSubmissionRequest
import com.prayatna.lookiesapp.domain.repository.UserRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class AddPartnerSubmissionUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(data: PartnerSubmissionRequest): DataResult<String> {
        return userRepository.submitPartnerApplication(
            partnerName = data.partnerName,
            partnerType = data.partnerType,
            locationId = data.locationId,
            portfolioLink = data.portfolioLink,
            imageLogo = data.imageLogo
        )
    }
}
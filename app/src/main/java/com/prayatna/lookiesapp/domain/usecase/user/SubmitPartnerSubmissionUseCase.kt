package com.prayatna.lookiesapp.domain.usecase.user

import com.prayatna.lookiesapp.domain.model.user.PartnerSubmissionParams
import com.prayatna.lookiesapp.domain.repository.UserRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class SubmitPartnerSubmissionUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(data: PartnerSubmissionParams): DataResult<String> {
        return userRepository.submitPartnerApplication(params = data)
    }
}
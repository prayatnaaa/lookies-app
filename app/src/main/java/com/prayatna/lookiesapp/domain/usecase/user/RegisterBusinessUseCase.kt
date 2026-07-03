package com.prayatna.lookiesapp.domain.usecase.user

import android.net.Uri
import com.prayatna.lookiesapp.data.remote.dto.response.user.RoleApplicationResponse
import com.prayatna.lookiesapp.domain.model.user.RoleApplicationInput
import com.prayatna.lookiesapp.domain.repository.UserRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class RegisterBusinessUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        input: RoleApplicationInput,
        kycFiles: List<Pair<String, Uri>>
    ): DataResult<RoleApplicationResponse> {
        return userRepository.registerBusiness(
            request = input,
            kycFiles = kycFiles
        )
    }
}

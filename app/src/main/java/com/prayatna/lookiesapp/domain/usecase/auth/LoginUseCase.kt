package com.prayatna.lookiesapp.domain.usecase.auth

import com.prayatna.lookiesapp.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.signIn(email, password)
}
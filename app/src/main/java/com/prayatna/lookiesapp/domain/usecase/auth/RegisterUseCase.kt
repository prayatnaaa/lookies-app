package com.prayatna.lookiesapp.domain.usecase.auth

import android.util.Patterns
import com.prayatna.lookiesapp.domain.model.auth.RegisterInput
import com.prayatna.lookiesapp.domain.model.auth.RegisterOutput
import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

private fun validateRegisterInput(input: RegisterInput): String? {
    if (input.fullName.isBlank()) return "Name cannot be empty!"

    if (input.email.isBlank()) return "Email cannot be empty!"
    if (!Patterns.EMAIL_ADDRESS.matcher(input.email).matches()) {
        return "Invalid email format"
    }

    if (input.password.isBlank()) return "Password cannot be empty!"
    if (input.password.length < 6) {
        return "Password requires minimum 6 characters"
    }

    if (input.verifyPassword.isBlank()) return "Confirm password cannot be empty!"
    if (input.password != input.verifyPassword) {
        return "Password and confirm password do not match"
    }

    return null
}

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(input: RegisterInput): DataResult<RegisterOutput> {

        val error = validateRegisterInput(input)
        if (error != null) {
            return DataResult.Error(error)
        }

        return authRepository.signUp(input)
    }
}
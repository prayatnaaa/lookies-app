package com.prayatna.lookiesapp.domain.usecase.auth

import android.util.Log
import android.util.Patterns
import com.prayatna.lookiesapp.domain.model.auth.LoginOutput
import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

private fun validateLoginInput(email: String, password: String): String? {
    if (email.isBlank()) return "Email cannot be empty!"
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Invalid email format"
    if (password.isBlank()) return "Password cannot be empty!"
    if (password.length < 6) return "Password requires minimum 6 characters"
    return null
}

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): DataResult<LoginOutput> {

        val error = validateLoginInput(email, password)
        if (error != null) {
            Log.e("LoginTest", "Error: $error")
            return DataResult.Error(error)
        }

        return authRepository.signIn(email, password)
    }
}
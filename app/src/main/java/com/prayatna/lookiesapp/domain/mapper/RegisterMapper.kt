package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.auth.RegisterRequest
import com.prayatna.lookiesapp.domain.model.auth.RegisterInput

fun RegisterInput.toDto(): RegisterRequest {
    return RegisterRequest(
        fullName = this.fullName,
        email = this.email,
        password = this.password,
        verifyPassword = this.verifyPassword
    )
}
package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.auth.LoginResponse
import com.prayatna.lookiesapp.domain.model.auth.LoginOutput

fun LoginResponse.toDomain() =
    LoginOutput(
        message = message,
        role = role,
        success = success
    )
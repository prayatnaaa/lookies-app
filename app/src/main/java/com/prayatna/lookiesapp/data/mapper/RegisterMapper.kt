package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.auth.RegisterResponse
import com.prayatna.lookiesapp.data.remote.dto.response.auth.RegisterResponseData
import com.prayatna.lookiesapp.domain.model.auth.RegisterData
import com.prayatna.lookiesapp.domain.model.auth.RegisterOutput

fun RegisterResponse.toDomain(): RegisterOutput {
    return RegisterOutput(
        status = this.status,
        message = this.message,
        data = this.data?.toDomain()
    )
}

fun RegisterResponseData.toDomain(): RegisterData {
    return RegisterData(
        fullName = this.fullName,
        email = this.email
    )
}
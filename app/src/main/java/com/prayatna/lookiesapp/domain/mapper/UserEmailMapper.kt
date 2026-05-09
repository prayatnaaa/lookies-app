package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.UserEmailDto
import com.prayatna.lookiesapp.domain.model.user.UserEmail

fun UserEmailDto.toDomain(): UserEmail {
    return UserEmail(
        id = id,
        email = email
    )
}

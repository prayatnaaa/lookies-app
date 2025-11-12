package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.domain.model.user.User
import com.prayatna.lookiesapp.data.remote.dto.UserDto

fun UserDto.asDomainModel(): User {
    return User (
        id = this.id,
        username = this.username,
        role = this.role
    )
}
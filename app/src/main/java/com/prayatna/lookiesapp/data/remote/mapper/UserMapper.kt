package com.prayatna.lookiesapp.data.remote.mapper

import com.prayatna.lookiesapp.data.model.User
import com.prayatna.lookiesapp.data.remote.dto.UserDto

fun UserDto.asDomainModel(): User {
    return User (
        id = this.id,
        username = this.username,
        role = this.role
    )
}
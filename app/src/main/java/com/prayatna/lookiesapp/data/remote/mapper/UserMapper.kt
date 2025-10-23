package com.prayatna.lookiesapp.data.remote.mapper

import com.prayatna.lookiesapp.data.model.User
import com.prayatna.lookiesapp.data.remote.dto.UserDto

fun UserDto.asDomainModel(): User {
    return User (
        id = this.id,
        username = this.username,
        fullName = this.fullName,
        profileUrl = this.profileUrl,
        address = this.address,
        bio = this.bio,
        role = this.role
    )
}
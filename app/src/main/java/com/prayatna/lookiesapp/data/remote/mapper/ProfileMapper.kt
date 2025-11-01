package com.prayatna.lookiesapp.data.remote.mapper

import com.prayatna.lookiesapp.data.remote.dto.ProfileDto
import com.prayatna.lookiesapp.domain.model.user.Profile

fun ProfileDto.asDomainModel(): Profile {
    return Profile(
        id = this.id,
        profileUrl = this.profileUrl,
        username = this.username,
        fullName = this.fullName,
        address = this.address,
        bio = this.bio,
    )
}

fun Profile.toDto(): ProfileDto = ProfileDto(
    id = id,
    username = username,
    fullName = fullName,
    bio = bio,
    address = address,
    profileUrl = profileUrl
)

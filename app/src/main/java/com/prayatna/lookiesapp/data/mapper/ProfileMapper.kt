package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.ProfileDto
import com.prayatna.lookiesapp.domain.model.user.Profile

fun ProfileDto.asDomainModel(): Profile {
    return Profile(
        id = this.id,
        profileUrl = this.profilePictureUrl,
        username = this.username,
        fullName = this.fullName,
        address = this.address,
        bio = this.bio,
        hasPartnerSub = this.hasPartnerSub,
        isArtist = this.isArtist
    )
}

fun Profile.toDto(): ProfileDto = ProfileDto(
    id = id,
    username = username,
    fullName = fullName,
    bio = bio,
    address = address,
    profilePictureUrl = profileUrl,
    hasPartnerSub = hasPartnerSub,
    isArtist = isArtist
)

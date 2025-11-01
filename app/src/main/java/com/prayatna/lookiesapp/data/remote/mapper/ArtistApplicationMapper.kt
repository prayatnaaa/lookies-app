package com.prayatna.lookiesapp.data.remote.mapper

import com.prayatna.lookiesapp.data.remote.dto.ArtistApplicationDto
import com.prayatna.lookiesapp.domain.model.artist.ArtistApplication

fun ArtistApplicationDto.asDomainModel(): ArtistApplication {
    return ArtistApplication(
        userId = this.userId,
        portoUrl = this.portoUrl,
        motiveLetter = this.motiveLetter,
        status = this.status,
        reviewerId = this.reviewerId,
        reviewNote = this.reviewNote,
        businessType = this.businessType
    )
}
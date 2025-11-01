package com.prayatna.lookiesapp.domain.model.artist

import com.prayatna.lookiesapp.data.remote.dto.Status

data class ArtistApplication(

    val userId: String?,

    val portoUrl: String?,

    val motiveLetter: String?,

    val status: Status = Status.PENDING,

    val reviewerId: String? = null,

    val reviewNote: String? = null,

    val businessType: String
)
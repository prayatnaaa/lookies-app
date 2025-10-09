package com.prayatna.lookiesapp.data.remote.model

import com.prayatna.lookiesapp.data.remote.dto.Status

data class ArtistApplication(

    val userId: String,

    val portoUrl: String?,

    val motiveLetter: String?,

    val status: Status = Status.PENDING,

    val reviewerId: String?,

    val reviewNote: String?,

    val businessType: String
)
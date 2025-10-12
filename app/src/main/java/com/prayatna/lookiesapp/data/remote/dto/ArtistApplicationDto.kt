package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistApplicationDto(

    @SerialName("user_id")
    val userId: String,

    @SerialName("portofolio_url")
    val portoUrl: String?,

    @SerialName("motivation_letter")
    val motiveLetter: String?,

    @SerialName("status")
    val status: Status = Status.PENDING,

    @SerialName("reviewed_by")
    val reviewerId: String?,

    @SerialName("review_notes")
    val reviewNote: String?,

    @SerialName("business_type")
    val businessType: String
)

@Serializable
enum class Status {
    PENDING,
    APPROVED,
    REJECTED
}

package com.prayatna.lookiesapp.domain.model.admin


data class GetKycDocument (
    val id: String,
    val businessId: String,
    val type: String,
    val country: String,
    val fileId: String,
    val createdAt: String
)
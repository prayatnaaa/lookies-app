package com.prayatna.lookiesapp.domain.model.message

data class Conversation(
    val id: String?,
    val userId: String,
    val merchantId: String,
    val createdAt: String?,
    val updatedAt: String?
)

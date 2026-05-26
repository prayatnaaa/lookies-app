package com.prayatna.lookiesapp.domain.model.admin

data class AdminTransaction(
    val id: String,
    val totalAmount: Double,
    val status: String,
    val createdAt: String,
    val userEmail: String?,
    val userFullName: String?,
    val paymentStatus: String?
)

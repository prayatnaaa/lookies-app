package com.prayatna.lookiesapp.domain.model.user

data class CreateUserAddressInput(
    val userId: String,
    val name: String,
    val addressLine: String,
    val province: String,
    val phoneNumber: String,
    val postalCode: String,
    val city: String,
    val isDefault: Boolean,
    val notes: String? = null,
)
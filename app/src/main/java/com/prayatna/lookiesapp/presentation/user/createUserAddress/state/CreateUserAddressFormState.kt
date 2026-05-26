package com.prayatna.lookiesapp.presentation.user.createUserAddress.state

data class CreateUserAddressFormState (
    val name: String = "",
    val addressLine: String = "",
    val province: String = "",
    val phoneNumber: String = "",
    val postalCode: String = "",
    val city: String = "",
    val isDefault: Boolean = true,
    val notes: String? = null,
)
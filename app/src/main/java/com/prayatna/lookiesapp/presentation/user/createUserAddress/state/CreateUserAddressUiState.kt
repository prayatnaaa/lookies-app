package com.prayatna.lookiesapp.presentation.user.createUserAddress.state

import com.prayatna.lookiesapp.domain.model.user.UserAddress

data class CreateUserAddressUiState (
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val userAddresses: List<UserAddress>? = null,
    val isSuccess: Boolean = false
)
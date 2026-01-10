package com.prayatna.lookiesapp.presentation.checkout.state


data class CheckoutUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val itemToBuy: CheckoutItemDisplay? = null,
    val checkoutSuccessData: Long? = null
)

data class CheckoutItemDisplay(
    val id: String,
    val title: String,
    val subtitle: String,
    val price: Double?,
    val imageUrl: String,
    val type: String
)
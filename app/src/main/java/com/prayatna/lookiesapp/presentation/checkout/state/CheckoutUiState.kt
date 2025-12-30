package com.prayatna.lookiesapp.presentation.checkout.state

import com.prayatna.lookiesapp.domain.model.transaction.CheckoutOutput

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val itemToBuy: CheckoutItemDisplay? = null,
    val checkoutSuccessData: CheckoutOutput? = null
)

data class CheckoutItemDisplay(
    val id: String,
    val title: String,
    val subtitle: String,
    val price: Double?,
    val imageUrl: String,
    val type: String
)
package com.prayatna.lookiesapp.presentation.main

import com.prayatna.lookiesapp.domain.model.User

data class MainUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null
)
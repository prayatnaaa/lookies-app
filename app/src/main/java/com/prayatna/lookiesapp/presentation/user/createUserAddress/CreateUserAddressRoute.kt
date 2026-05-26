package com.prayatna.lookiesapp.presentation.user.createUserAddress

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun CreateUserAddressRoute(
    viewModel: CreateUserAddressViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("address_added", true)
            navController.popBackStack()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message = message)
            viewModel.dismissError()
        }
    }

    CreateUserAddressScreen(
        uiState = uiState,
        formState = formState,
        onEvent = { event -> viewModel.onEvent(event) },
        onBackClick = { navController.popBackStack() }
    )

}
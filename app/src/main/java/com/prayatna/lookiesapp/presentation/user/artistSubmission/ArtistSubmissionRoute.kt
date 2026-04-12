package com.prayatna.lookiesapp.presentation.user.artistSubmission

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.user.artistSubmission.state.ArtistSubmissionEvent
import com.prayatna.lookiesapp.presentation.user.artistSubmission.state.ArtistSubmissionUiState

@Composable
fun ArtistSubmissionRoute(
    navController: NavController,
    viewModel: ArtistSubmissionViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is ArtistSubmissionUiState.Error) {
            val errorMessage = (uiState as ArtistSubmissionUiState.Error).message
            snackbarHostState.showSnackbar(message = errorMessage)
            viewModel.onEvent(ArtistSubmissionEvent.DismissError)
        }
    }

    ArtistSubmissionScreen(
        uiState = uiState,
        formState = formState,
        onEvent = { event ->
            when (event) {
                ArtistSubmissionEvent.OnBack -> {
                    navController.popBackStack()
                }
                else -> viewModel.onEvent(event)
            }
        },
        snackbarHostState = snackbarHostState
    )


}
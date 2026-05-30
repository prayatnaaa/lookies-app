package com.prayatna.lookiesapp.presentation.user.artistSubmission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.prayatna.lookiesapp.presentation.components.registerArtist.ArtistSubmissionContent
import com.prayatna.lookiesapp.presentation.components.registerBusiness.SuccessDialog
import com.prayatna.lookiesapp.presentation.user.artistSubmission.state.ArtistSubmissionEvent
import com.prayatna.lookiesapp.presentation.user.artistSubmission.state.ArtistSubmissionFormState
import com.prayatna.lookiesapp.presentation.user.artistSubmission.state.ArtistSubmissionUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistSubmissionScreen(
    uiState: ArtistSubmissionUiState,
    formState: ArtistSubmissionFormState,
    onEvent: (ArtistSubmissionEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            onEvent(ArtistSubmissionEvent.KycFileSelected(it))
        }
    }

    if (uiState is ArtistSubmissionUiState.Success) {
        SuccessDialog(
            message = "Your artist application has been submitted. We'll review it and get back to you.",
            onConfirm = {
                onEvent(ArtistSubmissionEvent.OnBack)
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Artist Application",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        content = { innerPadding ->
            ArtistSubmissionContent(
                modifier = Modifier.padding(innerPadding),
                formState = formState,
                isLoading = uiState is ArtistSubmissionUiState.Loading,
                onEvent = onEvent,
                onPickFileClick = { launcher.launch("*/*") },
                onSelectBankClick = { onEvent(ArtistSubmissionEvent.OnSelectBankClicked) }
            )
        }
    )
}

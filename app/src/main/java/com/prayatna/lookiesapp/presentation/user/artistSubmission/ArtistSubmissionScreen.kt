package com.prayatna.lookiesapp.presentation.user.artistSubmission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
    snackbarHostState: SnackbarHostState
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            onEvent(ArtistSubmissionEvent.KycFileSelected(it))
        }
    }

    if (uiState is ArtistSubmissionUiState.Success) {
        val response = uiState.response
        SuccessDialog(
            message = response.message,
            onConfirm = {
                onEvent(ArtistSubmissionEvent.DismissError)
                onEvent(ArtistSubmissionEvent.OnBack)
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Artist Registration", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(ArtistSubmissionEvent.OnBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        ArtistSubmissionContent(
            modifier = Modifier.padding(paddingValues),
            formState = formState,
            isLoading = uiState is ArtistSubmissionUiState.Loading,
            onEvent = onEvent,
            onPickFileClick = { launcher.launch("image/*") }
        )
    }
}

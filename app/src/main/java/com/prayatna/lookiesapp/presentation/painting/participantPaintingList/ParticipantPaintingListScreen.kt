package com.prayatna.lookiesapp.presentation.painting.participantPaintingList

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.paintingSubmission.PaintingSubmissionCard
import com.prayatna.lookiesapp.presentation.painting.participantPaintingList.state.DialogState
import com.prayatna.lookiesapp.utils.Constants
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun ParticipantPaintingListScreen(
    eventType: String? = null,
    eventId: String,
    businessId: String? = null,
    navController: NavController,
    viewModel: ParticipantPaintingListViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(eventId) {
        viewModel.loadPaintings(eventId)
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            snackbarHostState.showSnackbar("Action success")
        }
    }

    LaunchedEffect(Unit) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<String?>(
                "snackbar_message",
                null
            )
            ?.collect { message ->

                if (message != null) {

                    snackbarHostState.showSnackbar(message)

                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.remove<String>("snackbar_message")
                }
            }
    }

    Scaffold(
        bottomBar = {
            if (eventType == "self_exhibition") {
                Button(onClick = {
                    navController.navigate("${NavigationRoutes.SELF_EVENT_LIST}/${businessId}")
                }) {
                    Text("Upload Painting")
                }
            }
        },
        topBar = {
            BackTopBar(
                onBackClick = {
                    navController.popBackStack()
                },
                title = "Participant paintings"
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.eventPaintings) { painting ->
                            PaintingSubmissionCard(
                                item = painting,
                                isLoading = uiState.loadingPaintingId == painting.id,
                                onClick = {
                                    navController.navigate(
                                        "${NavigationRoutes.PARTNER_EXHIBITION_PAINTING_DETAIL}/${painting.id}"
                                    )
                                },
                                onApprove = {
                                    viewModel.showApproveDialog(painting.id)
                                },
                                onReject = {
                                    viewModel.showRejectDialog(painting.id)
                                }
                            )
                        }
                    }
                }
            }

            uiState.dialogState?.let { dialog ->
                AlertDialog(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    containerColor = MaterialTheme.colorScheme.surface,
                    onDismissRequest = { viewModel.dismissDialog() },
                    title = {
                        Text(
                            if (dialog is DialogState.Approve)
                                "Approve Painting"
                            else
                                "Reject Painting"
                        )
                    },
                    text = {
                        Text(
                            if (dialog is DialogState.Approve)
                                "Are you sure you want to approve this painting?"
                            else
                                "Are you sure you want to reject this painting?"
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            when (dialog) {
                                is DialogState.Approve ->
                                    viewModel.approvePainting(dialog.paintingId)
                                is DialogState.Reject ->
                                    viewModel.rejectPainting(dialog.paintingId)
                            }
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.dismissDialog() }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

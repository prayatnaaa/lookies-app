package com.prayatna.lookiesapp.presentation.painting.participantPaintingList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.paintingSubmission.PaintingSubmissionItem
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
        floatingActionButton = {
            if (eventType == "self_exhibition") {
                Button(onClick = {
                    navController.navigate(
                        "${
                            NavigationRoutes.INSERT_EVENT_PAINTINGS_ROUTE
                        }/${eventId}/${businessId}")
                }) {
                    Text("Add Event Artworks")
                }
            }
        },
        topBar = {
            BackTopBar(
                onBackClick = {
                    navController.popBackStack()
                },
                title = "Exhibition paintings"
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
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        itemsIndexed(uiState.eventPaintings) { index, painting ->

                            PaintingSubmissionItem(
                                item = painting,
                                showDivider = index != uiState.eventPaintings.lastIndex,
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

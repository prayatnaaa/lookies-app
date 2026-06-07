package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.partnerExhibition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.eventPainting.EventPaintingDetailContent
import com.prayatna.lookiesapp.presentation.components.eventPainting.PartnerShipmentActionBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.partnerExhibition.state.PartnerExhibitionPaintingEffect
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.partnerExhibition.state.PartnerExhibitionPaintingEvent
import com.prayatna.lookiesapp.presentation.offlineCheckout.navigateToOfflineCheckout
import com.prayatna.lookiesapp.presentation.unsoldArtworkReturn.navigateToUnsoldArtworkReturn
import com.prayatna.lookiesapp.utils.NavigationRoutes
import java.time.OffsetDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerExhibitionPaintingDetailScreen(
    navController: NavController,
    eventPaintingId: String,
    viewModel: PartnerExhibitionPaintingDetailViewModel = hiltViewModel()
) {

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    var showRejectSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var rejectReason by remember { mutableStateOf("") }
    var selectedPaintingId by remember { mutableStateOf<String?>(null) }

    // LOAD
    LaunchedEffect(eventPaintingId) {
        viewModel.onEvent(
            PartnerExhibitionPaintingEvent.Load(eventPaintingId)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {

                is PartnerExhibitionPaintingEffect.ShowResult -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("snackbar_message", event.message)

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "refresh",
                            true
                        )

                    navController.popBackStack()
                }

                is PartnerExhibitionPaintingEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        event.message,
                        withDismissAction = true
                    )
                }
            }
        }
    }

    if (showRejectSheet) {
        RejectPaintingBottomSheet(
            reason = rejectReason,
            onReasonChange = { rejectReason = it },
            onDismiss = {
                showRejectSheet = false
                rejectReason = ""
                selectedPaintingId = null
            },
            onConfirm = {
                selectedPaintingId?.let { id ->
                    viewModel.onEvent(
                        PartnerExhibitionPaintingEvent.Reject(
                            id = id,
                            reason = rejectReason
                        )
                    )
                }

                showRejectSheet = false
                rejectReason = ""
                selectedPaintingId = null
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Remove Artwork") },
            text = { Text("Are you sure you want to remove this artwork from the exhibition?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.onEvent(PartnerExhibitionPaintingEvent.Delete(eventPaintingId))
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BackTopBar(
                onBackClick = { navController.popBackStack() },
                title = "Artwork Detail"
            )
        },
        bottomBar = {
            state.data?.let { painting ->

                val isSelfExhibition = painting.participant.event.eventType.slug == "self_exhibition"
                val eventStart = try { OffsetDateTime.parse(painting.participant.event.startDate) } catch (e: Exception) { null }
                val isNotStarted = eventStart?.let { OffsetDateTime.now().isBefore(it) } ?: true

                Column {
                    if (isSelfExhibition && isNotStarted) {
                        Surface(
                            shadowElevation = 4.dp,
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                OutlinedButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { showDeleteDialog = true },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                                ) {
                                    if (state.actionLoading) {
                                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                    } else {
                                        Text("Remove from Exhibition")
                                    }
                                }
                            }
                        }
                    }

                    when (painting.status.lowercase()) {

                        "pending" -> {
                            PartnerPaintingDecisionActionBar(
                                onApprove = {
                                    viewModel.onEvent(
                                        PartnerExhibitionPaintingEvent.Approve(painting.id)
                                    )
                                },
                                onReject = {
                                    selectedPaintingId = painting.id
                                    showRejectSheet = true
                                }
                            )
                        }

                        "rejected" -> {}

                        "on_sale" -> {
                            if (state.data?.participant?.event?.eventFormat?.slug == "offline") {
                                MarkAsSoldButton(
                                    enable = state.data?.participant?.event?.status == "ongoing",
                                    onClick = {
                                        navController.navigateToOfflineCheckout(
                                            itemId = painting.id,
                                            quantity = 1
                                        )
                                    }
                                )
                            }
                        }

                        "unsold" -> {
                            PartnerShipmentActionBar(
                                enableReturningToCreator = false,
                                status = painting.status,
                                onManageShipment = {
                                    navController.navigateToUnsoldArtworkReturn(painting.id)
                                }
                            )
                        }

                        else -> {
                            PartnerShipmentActionBar(
                                enableReturningToCreator = false,
                                status = painting.status,
                                onManageShipment = {
                                    navController.navigate(
                                        NavigationRoutes.EXHIBITION_SHIPMENT + "/${painting.id}"
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->

        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularLoading()
                }
            }

            state.data != null -> {
                EventPaintingDetailContent(
                    painting = state.data!!,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun MarkAsSoldButton(
    onClick: () -> Unit,
    enable: Boolean = true
) {
    Surface(
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            Button(
                enabled = enable,
                modifier = Modifier.fillMaxWidth(),
                onClick = onClick
            ) {
                Text("Mark as Sold")
            }
        }
    }
}

@Composable
fun PartnerPaintingDecisionActionBar(
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Surface(
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
       Row(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedButton(
                onClick = onReject,
                modifier = Modifier.weight(1f)
            ) {
                Text("Reject")
            }

            Button(
                onClick = onApprove,
                modifier = Modifier.weight(1f)
            ) {
                Text("Approve")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RejectPaintingBottomSheet(
    reason: String,
    onReasonChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Reject Painting",
                style = MaterialTheme.typography.titleLarge
            )

            androidx.compose.material3.OutlinedTextField(
                value = reason,
                onValueChange = onReasonChange,
                label = { Text("Reason") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = onConfirm,
                    enabled = reason.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reject")
                }
            }
        }
    }
}

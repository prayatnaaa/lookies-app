package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.partnerExhibition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.eventPainting.EventPaintingDetailContent
import com.prayatna.lookiesapp.presentation.components.eventPainting.PartnerShipmentActionBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state.EventPaintingDetailEffect
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state.EventPaintingDetailEvent
import com.prayatna.lookiesapp.presentation.offlineCheckout.navigateToOfflineCheckout
import com.prayatna.lookiesapp.presentation.unsoldArtworkReturn.navigateToUnsoldArtworkReturn
import com.prayatna.lookiesapp.utils.NavigationRoutes

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
    var rejectReason by remember { mutableStateOf("") }
    var selectedPaintingId by remember { mutableStateOf<String?>(null) }

//    var resultMessage by remember { mutableStateOf<String?>(null) }

    // LOAD
    LaunchedEffect(eventPaintingId) {
        viewModel.onEvent(
            EventPaintingDetailEvent.Load(eventPaintingId)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {

                is EventPaintingDetailEffect.ShowResult -> {
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

                is EventPaintingDetailEffect.ShowError -> {
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
                        EventPaintingDetailEvent.Reject(
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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BackTopBar(
                onBackClick = { navController.popBackStack() },
                title = "Artwork Detail"
            )
        },
        bottomBar = {
            state.data?.let { painting ->

                when (painting.status.lowercase()) {

                    "pending" -> {
                        PartnerPaintingDecisionActionBar(
                            onApprove = {
                                viewModel.onEvent(
                                    EventPaintingDetailEvent.Approve(painting.id)
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
                        MarkAsSoldButton {
                            navController.navigateToOfflineCheckout(
                                itemId = painting.id,
                                quantity = 1
                            )
                        }
                    }

                    "unsold" -> {
                        PartnerShipmentActionBar(
                            status = painting.status,
                            onManageShipment = {
                                navController.navigateToUnsoldArtworkReturn(painting.id)
                            }
                        )
                    }

                    else -> {
                        PartnerShipmentActionBar(
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
    onClick: () -> Unit
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
package com.prayatna.lookiesapp.presentation.event.detailevent

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.detailevent.*
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.utils.NavigationRoutes
import kotlinx.coroutines.delay

@Composable
fun DetailEventScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DetailEventViewModel = hiltViewModel(),
    eventId: String,
) {
    val detailEventState by viewModel.state.collectAsStateWithLifecycle()
    val quantity by viewModel.quantityValue.collectAsStateWithLifecycle()
    val adminState by viewModel.adminState.collectAsStateWithLifecycle()
    val role by viewModel.roleState.collectAsStateWithLifecycle()

    var isTicketSheetOpen by rememberSaveable { mutableStateOf(false) }
    var showResultModal by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    var isRejectSheetOpen by rememberSaveable { mutableStateOf(false) }
    var rejectReason by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(eventId) {
        viewModel.getEvent(eventId)
        viewModel.getEventPaintings(eventId)
    }

    LaunchedEffect(adminState.success) {
        if (adminState.success != null) {
            val data = adminState.success
            showResultModal = true
            message = "Event has been ${data?.status}"
            delay(2000)
            navController.navigateUp()
        }
    }

    LaunchedEffect(adminState.error) {
        if (adminState.error != null) {
            val data = adminState.error
            showResultModal = true
            message = data!!
            delay(2000)
            navController.navigateUp()
        }
    }

    if (showResultModal) {
        CustomBottomSheet(
            onDismiss = { showResultModal = false },
            message = message,
            title = "",
            onConfirm = { showResultModal = false }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            BackTopBar(
                onBackClick = {
                navController.popBackStack()
            },
                title = "Detail event")
        },

        bottomBar = {
            detailEventState.info?.let { event ->
                DetailEventBottomBar(
                    role = role,
                    event = event,
                    onApprove = { viewModel.approveEvent(eventId) },
                    onReject = { isRejectSheetOpen = true },
                    onRegister = {
                        navController.navigate(
                            NavigationRoutes.REGISTER_EVENT +
                                    "/$eventId" +
                                    "/${event.maxPaintingPerArtist}" +
                                    "/${event.artistRegistrationFee}" +
                                    "/${event.organizer.id}"
                        )
                    },
                    onBuy = { isTicketSheetOpen = true },
                    onSeeArts = {}
                )
            }
        }
    ) { innerPadding ->

        when {
            detailEventState.isLoading -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularLoading()
                }
            }

            detailEventState.info != null -> {
                val event = detailEventState.info!!
                val paintings = detailEventState.paintings

                DetailEventContent(
                    modifier = Modifier.padding(innerPadding),
                    event = event,
                    paintings = paintings,
                    isUserArtist = role == "artist",
                    onPaintingClick = { id ->
                        navController.navigate("${NavigationRoutes.DETAIL_EVENT_PAINTING}/$id")
                    },
                    onPartnerClick = { id ->
                        navController.navigate(
                            "${NavigationRoutes.MESSAGES}/$id/${event.organizer.legalName}"
                        )
                    }
                )
            }

            detailEventState.detailEventError != null -> {
                Box(
                    modifier = modifier.padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = detailEventState.detailEventError ?: "Unknown error",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        // Ticket Bottom Sheet
        if (isTicketSheetOpen) {
            DetailEventBottomModal(
                onDismiss = { isTicketSheetOpen = false },
                value = quantity,
                onValueChange = { viewModel.setQuantityValue(it) },
                onBuyButtonClick = {
                    isTicketSheetOpen = false
                    navController.navigate(
                        "${NavigationRoutes.CHECKOUT}/event_ticket/$eventId/$quantity"
                    )
                }
            )
        }

        if (isRejectSheetOpen) {
            RejectEventBottomSheet(
                reason = rejectReason,
                onReasonChange = { rejectReason = it },
                onDismiss = { isRejectSheetOpen = false },
                onConfirm = {
                    isRejectSheetOpen = false
                    viewModel.rejectEvent(eventId, rejectReason)
                    rejectReason = ""
                }
            )
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun RejectEventBottomSheet(
    reason: String,
    onReasonChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    androidx.compose.material3.ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Reject Event",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

            androidx.compose.material3.OutlinedTextField(
                value = reason,
                onValueChange = onReasonChange,
                label = { Text("Rejection Reason") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                androidx.compose.material3.OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                
                androidx.compose.material3.Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    enabled = reason.isNotBlank(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = com.prayatna.lookiesapp.ui.theme.Maroon,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    )
                ) {
                    Text("Reject")
                }
            }
        }
    }
}

@Composable
private fun DetailEventBottomBar(
    role: String,
    event: com.prayatna.lookiesapp.domain.model.event.Event,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onRegister: () -> Unit,
    onBuy: () -> Unit,
    onSeeArts: () -> Unit
) {
    DetailEventFooter(
        showDecideButton = role == "admin",
        onApproveButtonClick = onApprove,
        onRejectButtonClick = onReject,

        showRegisterButton = role == "artist" &&
                event.eventType.slug == "open_call",

        isRegisterButtonEnabled = event.status == "published",
        onRegisterButtonClick = onRegister,

        showBuyButton = role != "admin" &&
                role != "partner" &&
                event.eventFormat.slug != "online",

        onBuyButtonClick = onBuy,
        onSeePaintingsClick = onSeeArts
    )
}
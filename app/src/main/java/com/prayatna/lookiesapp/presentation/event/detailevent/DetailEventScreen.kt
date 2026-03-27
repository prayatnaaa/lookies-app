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
            BackTopBar(navController = navController)
        },

        bottomBar = {
            detailEventState.info?.let { event ->
                DetailEventBottomBar(
                    role = role,
                    event = event,
                    onApprove = { viewModel.approveEvent(eventId) },
                    onReject = { viewModel.rejectEvent(eventId) },
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
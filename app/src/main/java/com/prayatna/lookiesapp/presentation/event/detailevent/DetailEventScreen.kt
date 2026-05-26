package com.prayatna.lookiesapp.presentation.event.detailevent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.detailevent.DetailEventBottomModal
import com.prayatna.lookiesapp.presentation.components.detailevent.DetailEventContent
import com.prayatna.lookiesapp.presentation.components.detailevent.DetailEventFooter
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.event.detailevent.state.DetailEventUiEvent
import com.prayatna.lookiesapp.ui.theme.Maroon
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun DetailEventScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DetailEventViewModel = hiltViewModel(),
    eventId: String,
) {
    val detailEventState by viewModel.state.collectAsStateWithLifecycle()
    val quantity by viewModel.quantityValue.collectAsStateWithLifecycle()
    val role by viewModel.roleState.collectAsStateWithLifecycle()
    val adminState by viewModel.adminState.collectAsStateWithLifecycle()

    var isTicketSheetOpen by rememberSaveable { mutableStateOf(false) }
    var isRejectSheetOpen by rememberSaveable { mutableStateOf(false) }
    var rejectReason by rememberSaveable { mutableStateOf("") }

    var resultState by remember {
        mutableStateOf<DetailEventUiEvent.ShowResult?>(null)
    }

    // Load data
    LaunchedEffect(eventId) {
        viewModel.getEvent(eventId)
        viewModel.getEventPaintings(eventId)
    }

    // Collect UI Event (FIXED)
    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            if (event is DetailEventUiEvent.ShowResult) {
                resultState = event
            }
        }
    }

    // =========================
    // RESULT MODAL (GLOBAL)
    // =========================
    resultState?.let { result ->
        CustomBottomSheet(
            title = result.message,
            message = "",
            confirmText = "Ok",
            onConfirm = {
                resultState = null
                navController.popBackStack()
            },
            onDismiss = {
                resultState = null
                navController.popBackStack()
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            BackTopBar(
                onBackClick = { navController.popBackStack() },
                title = "Detail event"
            )
        },

        bottomBar = {
            detailEventState.info?.let { event ->
                DetailEventBottomBar(
                    role = role,
                    event = event,
                    isApproveLoading = adminState.isLoading,
                    isRejectLoading = adminState.isLoading,
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
                    onSeeArts = {
                        navController.navigate("${NavigationRoutes.EVENT_PAINTING_LIST}/$eventId?eventType=${event.eventType.slug}")
                    }
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

        // =========================
        // OTHER SHEETS (LOCK WHEN RESULT SHOWING)
        // =========================
        if (resultState == null) {

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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RejectEventBottomSheet(
    reason: String,
    onReasonChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    ModalBottomSheet(
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
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
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
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    enabled = reason.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Maroon,
                        contentColor = Color.White
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
    event: Event,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    isApproveLoading: Boolean = false,
    isRejectLoading: Boolean = false,
    onRegister: () -> Unit,
    onBuy: () -> Unit,
    onSeeArts: () -> Unit
) {
    DetailEventFooter(
        showDecideButton = role == "admin",

        onApproveButtonClick = {
            if (!isApproveLoading) onApprove()
        },
        onRejectButtonClick = {
            if (!isRejectLoading) onReject()
        },

        showRegisterButton = role == "artist" &&
                event.eventType.slug == "open_call",

        isRegisterButtonEnabled = event.status == "published",
        onRegisterButtonClick = onRegister,

        showBuyButton = role != "admin" &&
                event.eventFormat.slug != "online",

        onBuyButtonClick = onBuy,
        onSeePaintingsClick = onSeeArts
    )
}
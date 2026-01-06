package com.prayatna.lookiesapp.presentation.partner.editEvent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.CustomDialog
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.createEvent.DetailEventForm
import com.prayatna.lookiesapp.presentation.components.createEvent.EventAboutForm
import com.prayatna.lookiesapp.presentation.components.createEvent.EventLocationForm
import com.prayatna.lookiesapp.presentation.components.createEvent.ParticipationRulesForm
import com.prayatna.lookiesapp.presentation.components.createEvent.PricingForm
import com.prayatna.lookiesapp.presentation.partner.editEvent.state.EditEventFormEvent
import com.prayatna.lookiesapp.utils.Constants
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun EditEventScreen(
    navController: NavController,
    eventId: String,
    viewModel: EditEventViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(eventId) {
        viewModel.onEvent(EditEventFormEvent.LoadEventMeta)
        viewModel.loadEvent(eventId)
    }

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogTitle by remember { mutableStateOf("") }
    LaunchedEffect(uiState) {
        when {
            uiState.isSuccess -> {
                dialogTitle = "Success"
                dialogMessage = "Event created successfully"
                showDialog = true
            }

            uiState.errorMessage != null -> {
                dialogTitle = "Error"
                dialogMessage = uiState.errorMessage!!
                showDialog = true
            }
        }
    }


    if (showDialog) {
        CustomDialog(
            title = dialogTitle,
            message = dialogMessage,
            onConfirm = {
                showDialog = false

                if (uiState.isSuccess) {
                    navController.navigateUp()
                }
            },
            onDismiss = {
                showDialog = false
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BackTopBar(
                navController = navController,
                title = "Edit Event"
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {

            item {
                DetailEventForm(
                    eventName = formState.title,
                    onEventNameChange = {
                        viewModel.onEvent(EditEventFormEvent.TitleChanged(it))
                    },
                    imageUriString = formState.bannerImage,
                    onImageClick = {},
                    startDate = formState.startDate,
                    onStartDateChange = {
                        viewModel.onEvent(EditEventFormEvent.StartDateChanged(it))
                    },
                    endDate = formState.endDate,
                    onEndDateChange = {
                        viewModel.onEvent(EditEventFormEvent.EndDateChanged(it))
                    },
                    eventTypes = formState.eventTypes,
                    selectedEventTypeId = formState.eventType,
                    onEventTypeChange = {
                        viewModel.onEvent(EditEventFormEvent.EventTypeChanged(it))
                    },
                    eventFormats = formState.eventFormats,
                    selectedEventFormatId = formState.eventFormat,
                    onEventFormatChange = {
                        viewModel.onEvent(EditEventFormEvent.EventFormatChanged(it))
                    }
                )
            }

            item {
                EventLocationForm(
                    locationName = formState.location,
                    onLocationNameChange = {
                        viewModel.onEvent(EditEventFormEvent.LocationChanged(it))
                    },
                    locationUrl = formState.locationUrl,
                    onLocationUrlChange = {
                        viewModel.onEvent(EditEventFormEvent.LocationUrlChanged(it))
                    }
                )
            }

            val isSelfExhibition = uiState.data?.eventFormat?.slug == "self_exhibition"

            item {
                ParticipationRulesForm(
                    maxParticipants = formState.maxParticipant,
                    onMaxParticipantsChange = {
                        viewModel.onEvent(EditEventFormEvent.MaxParticipantChanged(it))
                    },
                    maxPainting = formState.maxPainting,
                    onMaxPaintingChange = {
                        viewModel.onEvent(EditEventFormEvent.MaxPaintingChanged(it))
                    },
                    maxPaintingPerArtist = formState.maxPaintingPerArtist,
                    onMaxPaintingPerArtistChange = {
                        viewModel.onEvent(
                            EditEventFormEvent.MaxPaintingPerArtistChanged(it)
                        )
                    },
                    isSelfExhibition = isSelfExhibition
                )
            }

            val isOnline = uiState.data?.eventFormat?.slug == "online"

            item {
                PricingForm(
                    ticketPrice = formState.ticketPrice,
                    onTicketPriceChange = {
                        viewModel.onEvent(EditEventFormEvent.TicketPriceChanged(it))
                    },
                    artistRegistrationFee = formState.artistRegistrationFee,
                    onArtistRegistrationFeeChange = {
                        viewModel.onEvent(
                            EditEventFormEvent.ArtistRegistrationFeeChanged(it)
                        )
                    },
                    isSelfExhibition = isSelfExhibition,
                    isOnlineEvent = isOnline
                )
            }

            item {
                EventAboutForm(
                    eventDescription = formState.about,
                    onEventDescriptionChange = {
                        viewModel.onEvent(EditEventFormEvent.AboutChanged(it))
                    }
                )
            }

            if (uiState.data != null) {
                val event = uiState.data
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            enabled = !uiState.isLoading,
                            onClick = {
                                viewModel.onEvent(EditEventFormEvent.Submit)
                            }
                        ) {
                            Text(
                                text = if (uiState.isLoading) "Updating..." else "Update Event",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Button(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            enabled = !uiState.isLoading && event?.status != "pending_validation",
                                    onClick = {
                                navController.navigate("${NavigationRoutes.PARTICIPANT_LIST}/$eventId")
                            }
                        ) {
                            Text(
                                text = "Participants",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

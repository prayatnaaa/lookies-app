package com.prayatna.lookiesapp.presentation.partner.editEvent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.prayatna.lookiesapp.presentation.components.createEvent.RevenueShareForm
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
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

    LaunchedEffect(Unit) {
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
                dialogMessage = "Event updated successfully"
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
                dialogMessage = ""
                dialogTitle = ""

                if (uiState.isSuccess) {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("shouldRefresh", true)
                    navController.navigateUp()
                    navController.navigateUp()
                }
            },
            onDismiss = {
                showDialog = false
                dialogMessage = ""
                dialogTitle = ""
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BackTopBar(
                onBackClick = {
                    navController.popBackStack()
                },
                title = "Edit Event"
            )
        }
    ) { innerPadding ->

        if (uiState.isLoading) {
            CircularLoading()
        }

        LazyColumn(
            modifier = Modifier
                .imePadding()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val event = uiState.data

            if (event?.status == "rejected" && !event.rejectionReason.isNullOrBlank()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFC62828)
                        )
                        androidx.compose.foundation.layout.Column {
                            Text(
                                text = "Event Rejected",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFFC62828)
                            )
                            Text(
                                text = event.rejectionReason,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFB71C1C)
                            )
                        }
                    }
                }
            }

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
                    paintingSubmissionDeadline = formState.paintingSubmissionDeadline,
                    onPaintingSubmissionDeadlineChange = {
                        viewModel.onEvent(EditEventFormEvent.PaintingSubmissionDeadline(it))
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

            val selectedEventFormat =
                formState.eventFormats.find { it.id.toString() == formState.eventFormat }
            val isOnlineEvent = selectedEventFormat?.slug == "online"

            if (!isOnlineEvent && selectedEventFormat?.slug != null) {
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
            }
            val selectedEventType =
                formState.eventTypes.find { it.id.toString() == formState.eventType }

            val isSelfExhibition = selectedEventType?.slug == "self_exhibition"

            if (formState.eventType.isNotEmpty()) {
                item {
                    ParticipationRulesForm(
                        isSelfExhibition = isSelfExhibition,
//                        maxParticipants = formState.maxParticipant,
//                        onMaxParticipantsChange = {
//                            viewModel.onEvent(EditEventFormEvent.MaxParticipantChanged(it))
//                        },
                        maxPainting = formState.maxPainting,
                        onMaxPaintingChange = {
                            viewModel.onEvent(EditEventFormEvent.MaxPaintingChanged(it))
                        },
                        maxPaintingPerArtist = formState.maxPaintingPerArtist,
                        onMaxPaintingPerArtistChange = {
                            viewModel.onEvent(
                                EditEventFormEvent.MaxPaintingPerArtistChanged(
                                    it
                                )
                            )
                        }
                    )
                }
            }

            if (formState.eventFormat.isNotEmpty()) {
                item {
                    PricingForm(
                        isOnlineEvent = isOnlineEvent,
                        isSelfExhibition = isSelfExhibition,
                        ticketPrice = formState.ticketPrice,
                        onTicketPriceChange = {
                            viewModel.onEvent(EditEventFormEvent.TicketPriceChanged(it))
                        },
                        artistRegistrationFee = formState.artistRegistrationFee,
                        onArtistRegistrationFeeChange = {
                            viewModel.onEvent(
                                EditEventFormEvent.ArtistRegistrationFeeChanged(
                                    it
                                )
                            )
                        }
                    )
                }
            }

            item {
                EventAboutForm(
                    eventDescription = formState.about,
                    onEventDescriptionChange = {
                        viewModel.onEvent(EditEventFormEvent.AboutChanged(it))
                    }
                )
            }

            item {
                RevenueShareForm(
                    isSelfExhibition = isSelfExhibition,
                    isOnline = isOnlineEvent,
                    paintingArtistPercent = formState.paintingArtistPercent,
                    onPaintingArtistPercentChange = {
                        viewModel.onEvent(EditEventFormEvent.PaintingArtistPercentChanged(it))
                    },
                    paintingEventPercent = formState.paintingEventPercent,
                    onPaintingEventPercentChange = {
                        viewModel.onEvent(EditEventFormEvent.PaintingEventPercentChanged(it))
                    },
                    paintingPlatformPercent = formState.paintingPlatformPercent,
                    onPaintingPlatformPercentChange = {
                        viewModel.onEvent(EditEventFormEvent.PaintingPlatformPercentChanged(it))
                    },
                    ticketArtistPercent = formState.ticketArtistPercent,
                    onTicketArtistPercentChange = {
                        viewModel.onEvent(EditEventFormEvent.TicketArtistPercentChanged(it))
                    },
                    ticketEventPercent = formState.ticketEventPercent,
                    onTicketEventPercentChange = {
                        viewModel.onEvent(EditEventFormEvent.TicketEventPercentChanged(it))
                    },
                    ticketPlatformPercent = formState.ticketPlatformPercent,
                    onTicketPlatformPercentChange = {
                        viewModel.onEvent(EditEventFormEvent.TicketPlatformPercentChanged(it))
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
                                text = when {
                                    uiState.isLoading -> "Submitting..."
                                    event?.status == "rejected" -> "Resubmit"
                                    else -> "Update Event"
                                },
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Button(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            enabled = !uiState.isLoading && event?.eventType?.slug == "self_exhibition",
                                    onClick = {
                                navController.navigate(
                                    "${
                                        NavigationRoutes.INSERT_EVENT_PAINTINGS_ROUTE
                                    }/${eventId}/${event?.organizer?.id}")
                            }
                        ) {
                            Text(
                                text = "Add Paintings",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

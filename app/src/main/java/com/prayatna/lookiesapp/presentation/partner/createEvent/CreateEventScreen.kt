package com.prayatna.lookiesapp.presentation.partner.createEvent

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.prayatna.lookiesapp.presentation.partner.createEvent.state.CreateEventFormEvent
import com.prayatna.lookiesapp.utils.Constants

@Composable
fun CreateEventScreen(
    navController: NavController,
    viewModel: CreateEventViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogTitle by remember { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.onEvent(CreateEventFormEvent.BannerChanged(uri))
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(CreateEventFormEvent.LoadEventMeta)
    }

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
                title = "Create event"
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    DetailEventForm(
                        eventName = formState.title,
                        onEventNameChange = {
                            viewModel.onEvent(CreateEventFormEvent.TitleChanged(it))
                        },
                        imageUri = formState.bannerUri,
                        onImageClick = {
                            launcher.launch("image/*")
                        },
                        startDate = formState.startDate,
                        onStartDateChange = {
                            viewModel.onEvent(CreateEventFormEvent.StartDateChanged(it))
                        },
                        endDate = formState.endDate,
                        onEndDateChange = {
                            viewModel.onEvent(CreateEventFormEvent.EndDateChanged(it))
                        },
                        eventTypes = formState.eventTypes,
                        selectedEventTypeId = formState.eventType,
                        onEventTypeChange = {
                            viewModel.onEvent(CreateEventFormEvent.EventTypeChanged(it))
                        },
                        eventFormats = formState.eventFormats,
                        selectedEventFormatId = formState.eventFormat,
                        onEventFormatChange = {
                            viewModel.onEvent(CreateEventFormEvent.EventFormatChanged(it))
                        }
                    )
                }
                item {
                   EventLocationForm(
                       locationName = formState.location,
                       onLocationNameChange = {
                           viewModel.onEvent(CreateEventFormEvent.LocationChanged(it))
                       },
                       locationUrl = formState.locationUrl,
                       onLocationUrlChange = {
                           viewModel.onEvent(CreateEventFormEvent.LocationUrlChanged(it))
                       }
                   )
                }
                val selectedEventType =
                    formState.eventTypes.find { it.id.toString() == formState.eventType }

                val isSelfExhibition = selectedEventType?.slug == "self_exhibition"

                if (!isSelfExhibition && formState.eventType.isNotEmpty()) {
                    item {
                        ParticipationRulesForm(
                            maxParticipants = formState.maxParticipant,
                            onMaxParticipantsChange = {
                                viewModel.onEvent(CreateEventFormEvent.MaxParticipantChanged(it))
                            },
                            maxPainting = formState.maxPainting,
                            onMaxPaintingChange = {
                                viewModel.onEvent(CreateEventFormEvent.MaxPaintingChanged(it))
                            },
                            maxPaintingPerArtist = formState.maxPaintingPerArtist,
                            onMaxPaintingPerArtistChange = {
                                viewModel.onEvent(
                                    CreateEventFormEvent.MaxPaintingPerArtistChanged(
                                        it
                                    )
                                )
                            }
                        )
                    }
                }
                val selectedEventFormat =
                    formState.eventFormats.find { it.id.toString() == formState.eventFormat }

                val isOnlineEvent = selectedEventFormat?.slug == "online"

                if (!isOnlineEvent && formState.eventFormat.isNotEmpty()) {
                    item {
                        PricingForm(
                            ticketPrice = formState.ticketPrice,
                            onTicketPriceChange = {
                                viewModel.onEvent(CreateEventFormEvent.TicketPriceChanged(it))
                            },
                            artistRegistrationFee = formState.artistRegistrationFee,
                            onArtistRegistrationFeeChange = {
                                viewModel.onEvent(
                                    CreateEventFormEvent.ArtistRegistrationFeeChanged(
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
                           viewModel.onEvent(CreateEventFormEvent.AboutChanged(it))
                       }
                   )
                }

                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
//                        enabled = formState.isValid && !uiState.isLoading,
                        onClick = {
                            viewModel.onEvent(CreateEventFormEvent.Submit)
                        },
                        content = {
                            Text(text = "Submit",
                                style = MaterialTheme.typography.titleMedium)
                        }
                    )
                }
            }
        }
    )
}
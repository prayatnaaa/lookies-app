package com.prayatna.lookiesapp.presentation.partner.createEvent

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.createEvent.DetailEventForm
import com.prayatna.lookiesapp.presentation.components.createEvent.EventAboutForm
import com.prayatna.lookiesapp.presentation.components.createEvent.EventLocationForm
import com.prayatna.lookiesapp.presentation.components.createEvent.ParticipationRulesForm
import com.prayatna.lookiesapp.presentation.components.createEvent.PricingForm
import com.prayatna.lookiesapp.presentation.components.createEvent.RevenueShareForm
import com.prayatna.lookiesapp.presentation.partner.createEvent.state.CreateEventEffect
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

    // Derived values
    val selectedEventFormat =
        formState.eventFormats.find { it.id.toString() == formState.eventFormat }
    val isOnlineEvent = selectedEventFormat?.slug == "online"
    val selectedEventType =
        formState.eventTypes.find { it.id.toString() == formState.eventType }
    val isSelfExhibition = selectedEventType?.slug == "self_exhibition"
    val isOpenCall = selectedEventType?.slug == "open_call"

    // Progress tracking
    val totalSections = 7
    val filledSections by remember(formState, isOpenCall) {
        derivedStateOf {
            var count = 0
            // 1. Event Details: title + banner + dates + type + format + (deadlines if opencall)
            val basicDetails = formState.title.isNotBlank() && formState.bannerUri != null &&
                formState.startDate.isNotBlank() && formState.endDate.isNotBlank()
            val registrationDetails = !isOpenCall || (
                !formState.paintingSubmissionDeadline.isNullOrBlank() &&
                !formState.registrationStartDate.isNullOrBlank() &&
                !formState.registrationEndDate.isNullOrBlank()
            )
            if (basicDetails && registrationDetails) count++
            
            // 2. Event type & format selected
            if (formState.eventType.isNotBlank() && formState.eventFormat.isNotBlank()) count++
            // 3. Location (only for non-online)
            if (isOnlineEvent || formState.location.isNotBlank()) count++
            // 4. Participation rules
            if (formState.maxPainting?.isNotBlank() == true) count++
            // 5. Pricing
            if (isOnlineEvent || formState.ticketPrice?.isNotBlank() == true) count++
            // 6. Revenue share (painting totals 100)
            val paintingTotal =
                formState.paintingArtistPercent + formState.paintingEventPercent + (formState.paintingPlatformPercent ?: 0)
            if (paintingTotal == 100) count++
            // 7. About
            if (formState.about.isNotBlank()) count++
            count
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(CreateEventFormEvent.LoadEventMeta)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { event ->
            when (event) {
                CreateEventEffect.NavigateBack -> {

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "snackbar_message",
                            "Event created successfully!"
                        )

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("shouldRefresh", true)

                    navController.popBackStack()
                }

                is CreateEventEffect.ShowError -> {
                    dialogTitle = "Error"
                    dialogMessage = event.message
                    showDialog = true
                }
            }
        }
    }

    if (showDialog) {
        CustomBottomSheet(
            title = dialogTitle,
            message = dialogMessage,
            onConfirm = {
                showDialog = false
                if (uiState.isSuccess) {
                    navController.navigateUp()
                }
            },
            onDismiss = {
                dialogTitle = ""
                dialogMessage = ""
                showDialog = false
            }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets.ime,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BackTopBar(
                onBackClick = {
                    navController.popBackStack()
                },
                title = "Create Event"
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .imePadding()
            ) {
                // ── Progress Indicator ──────────────────────────
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Form Progress",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$filledSections / $totalSections sections",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { filledSections.toFloat() / totalSections },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            strokeCap = StrokeCap.Round,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }

                    Text(
                        text = "Complete all sections to create your event.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // ── 1. Event Details ────────────────────────────
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
                        },
                        paintingSubmissionDeadline = formState.paintingSubmissionDeadline,
                        onPaintingSubmissionDeadlineChange = {
                            viewModel.onEvent(CreateEventFormEvent.PaintingSubmissionDeadlineChanged(it))
                        },
                        registrationStartDate = formState.registrationStartDate,
                        onRegistrationStartDateChange = {
                            viewModel.onEvent(CreateEventFormEvent.RegistrationStartDateChanged(it))
                        },
                        registrationEndDate = formState.registrationEndDate,
                        onRegistrationEndDateChange = {
                            viewModel.onEvent(CreateEventFormEvent.RegistrationEndDateChanged(it))
                        },
                        isSelfExhibition = isSelfExhibition,
                        isOpenCall = isOpenCall
                    )
                }

                // ── 2. Event Location (non-online only) ─────────
                item {
                    AnimatedVisibility(
                        visible = !isOnlineEvent && selectedEventFormat?.slug != null,
                        enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                        exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
                    ) {
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
                }

                // ── 3. Participation Rules ──────────────────────
                item {
                    AnimatedVisibility(
                        visible = formState.eventType.isNotEmpty(),
                        enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                        exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
                    ) {
                        ParticipationRulesForm(
                            isSelfExhibition = isSelfExhibition,
                            maxPainting = formState.maxPainting,
                            onMaxPaintingChange = {
                                viewModel.onEvent(CreateEventFormEvent.MaxPaintingChanged(it))
                            },
                            maxPaintingPerArtist = formState.maxPaintingPerArtist,
                            onMaxPaintingPerArtistChange = {
                                viewModel.onEvent(
                                    CreateEventFormEvent.MaxPaintingPerArtistChanged(it)
                                )
                            }
                        )
                    }
                }

                // ── 4. Pricing & Fees ───────────────────────────
                item {
                    AnimatedVisibility(
                        visible = formState.eventFormat.isNotEmpty(),
                        enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                        exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
                    ) {
                        PricingForm(
                            isOnlineEvent = isOnlineEvent,
                            isSelfExhibition = isSelfExhibition,
                            ticketPrice = formState.ticketPrice,
                            onTicketPriceChange = {
                                viewModel.onEvent(CreateEventFormEvent.TicketPriceChanged(it))
                            },
                            artistRegistrationFee = formState.artistRegistrationFee,
                            onArtistRegistrationFeeChange = {
                                viewModel.onEvent(
                                    CreateEventFormEvent.ArtistRegistrationFeeChanged(it)
                                )
                            }
                        )
                    }
                }

                // ── 5. Revenue Sharing ────────────────────
                item {
                    AnimatedVisibility(
                        visible = formState.eventType.isNotEmpty(),
                        enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                        exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
                    ) {
                        RevenueShareForm(
                            isSelfExhibition = isSelfExhibition,
                            isOnline = isOnlineEvent,
                            paintingArtistPercent = formState.paintingArtistPercent,
                            onPaintingArtistPercentChange = {
                                viewModel.onEvent(
                                    CreateEventFormEvent.PaintingArtistPercentChanged(it)
                                )
                            },
                            paintingEventPercent = formState.paintingEventPercent,
                            onPaintingEventPercentChange = {
                                viewModel.onEvent(
                                    CreateEventFormEvent.PaintingEventPercentChanged(it)
                                )
                            },
                            paintingPlatformPercent = formState.paintingPlatformPercent,
                            onPaintingPlatformPercentChange = {
                                viewModel.onEvent(
                                    CreateEventFormEvent.PaintingPlatformPercentChanged(it)
                                )
                            },
                            ticketArtistPercent = formState.ticketArtistPercent,
                            onTicketArtistPercentChange = {
                                viewModel.onEvent(
                                    CreateEventFormEvent.TicketArtistPercentChanged(it)
                                )
                            },
                            ticketEventPercent = formState.ticketEventPercent,
                            onTicketEventPercentChange = {
                                viewModel.onEvent(
                                    CreateEventFormEvent.TicketEventPercentChanged(it)
                                )
                            },
                            ticketPlatformPercent = formState.ticketPlatformPercent,
                            onTicketPlatformPercentChange = {
                                viewModel.onEvent(
                                    CreateEventFormEvent.TicketPlatformPercentChanged(it)
                                )
                            }
                        )
                    }
                }

                // ── 6. About ────────────────────────────────────
                item {
                    EventAboutForm(
                        eventDescription = formState.about,
                        onEventDescriptionChange = {
                            viewModel.onEvent(CreateEventFormEvent.AboutChanged(it))
                        }
                    )
                }

                // ── Submit Button ───────────────────────────────
                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        enabled = formState.isValid && !uiState.isLoading,
                        onClick = {
                            viewModel.onEvent(CreateEventFormEvent.Submit)
                        },
                        content = {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Text(
                                    text = "Create Event",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    )
                }

                // Bottom spacing
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    )
}

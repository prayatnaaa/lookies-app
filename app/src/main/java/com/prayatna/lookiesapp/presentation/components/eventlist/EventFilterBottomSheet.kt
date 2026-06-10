package com.prayatna.lookiesapp.presentation.components.eventlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.partner.FilterItem
import com.prayatna.lookiesapp.presentation.event.eventlist.state.EventListEvent
import com.prayatna.lookiesapp.presentation.event.eventlist.state.EventListUiState
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventFilterBottomSheet(
    uiState: EventListUiState,
    onEvent: (EventListEvent) -> Unit,
    onDismiss: () -> Unit
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Advanced Filters",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Location Filter
            OutlinedTextField(
                value = uiState.selectedLocation ?: "",
                onValueChange = {
                    onEvent(EventListEvent.OnLocationChange(it))
                },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("e.g. Jakarta, Bandung")
                },
                shape = RoundedCornerShape(12.dp)
            )

            // Event Type Filter
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Event Type",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterItem(
                            title = "All",
                            selected = uiState.selectedEventType == null,
                            onClick = { onEvent(EventListEvent.OnEventTypeSelected(null)) }
                        )
                    }
                    items(uiState.eventTypes) { type ->
                        FilterItem(
                            title = type.name,
                            selected = uiState.selectedEventType == type.name,
                            onClick = { onEvent(EventListEvent.OnEventTypeSelected(type.name)) }
                        )
                    }
                }
            }

            // Event Format Filter
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Event Format",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterItem(
                            title = "All",
                            selected = uiState.selectedEventFormat == null,
                            onClick = { onEvent(EventListEvent.OnEventFormatSelected(null)) }
                        )
                    }
                    items(uiState.eventFormats) { format ->
                        FilterItem(
                            title = format.name,
                            selected = uiState.selectedEventFormat == format.name,
                            onClick = { onEvent(EventListEvent.OnEventFormatSelected(format.name)) }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // START DATE
                OutlinedTextField(
                    enabled = false,
                    value = uiState.startDate ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Start Date") },
                    placeholder = { Text("YYYY-MM-DD") },
                    modifier = Modifier
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                showStartDatePicker = true
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    interactionSource = remember { MutableInteractionSource() }
                )

                // END DATE
                OutlinedTextField(
                    enabled = false,
                    value = uiState.endDate ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("End Date") },
                    placeholder = { Text("YYYY-MM-DD") },
                    modifier = Modifier
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                showEndDatePicker = true
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    interactionSource = remember { MutableInteractionSource() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onEvent(EventListEvent.OnResetFilters)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }

                Button(
                    onClick = {
                        onEvent(EventListEvent.OnApplyFilters)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Apply Filters")
                }
            }
        }
    }

    // Start Date Picker
    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = {
                showStartDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formattedDate =
                                Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                    .toString()

                            onEvent(
                                EventListEvent.OnStartDateChange(
                                    formattedDate
                                )
                            )
                        }

                        showStartDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showStartDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // End Date Picker
    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = {
                showEndDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formattedDate =
                                Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                    .toString()

                            onEvent(
                                EventListEvent.OnEndDateChange(
                                    formattedDate
                                )
                            )
                        }

                        showEndDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEndDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

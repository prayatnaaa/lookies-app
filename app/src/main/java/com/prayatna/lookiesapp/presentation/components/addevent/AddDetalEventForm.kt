package com.prayatna.lookiesapp.presentation.components.addevent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AddDetailEventForm(
    modifier: Modifier = Modifier,
    locationUrlValue: String,
    onLocationUrlChange: (String) -> Unit,
    ticketQuantityValue: String,
    onTicketQuantityChange: (String) -> Unit,
    startTimeValue: String,
    onStartTimeChange: (String) -> Unit,
    endTimeValue: String,
    onEndTimeChange: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(vertical = 24.dp, horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        AddEventTextField(
            label = "Location Url",
            value = locationUrlValue,
            onValueChange = onLocationUrlChange
        )

        AddEventTextField(
            label = "Ticket quantity",
            value = ticketQuantityValue,
            onValueChange = onTicketQuantityChange
        )

        AddEventDateRangePicker(
            label = "Event Date Range",
            startDate = startTimeValue,
            endDate = endTimeValue,
            onStartDateChange = onStartTimeChange,
            onEndDateChange = onEndTimeChange
        )
    }
}

@Preview
@Composable
fun AddDetailEventPreview() {
    AddDetailEventForm(
        locationUrlValue = "",
        onLocationUrlChange = {},
        ticketQuantityValue = "",
        onTicketQuantityChange = {},
        startTimeValue = "",
        onStartTimeChange = {},
        endTimeValue = "",
        onEndTimeChange = {}
    )
}
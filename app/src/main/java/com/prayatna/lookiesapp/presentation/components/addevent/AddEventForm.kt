package com.prayatna.lookiesapp.presentation.components.addevent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AddEventForm(
    modifier: Modifier = Modifier,
    titleValue: String,
    onTitleChange: (String) -> Unit,
    locationValue: String,
    onLocationChange: (String) -> Unit,
    ticketPriceValue: String,
    onTicketPriceChange: (String) -> Unit,
    registrationFee: String,
    onRegistrationFeeChange: (String) -> Unit
) {
    Column (
        modifier = modifier
            .padding(vertical = 24.dp, horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        AddEventTextField(
            label = "Title",
            value = titleValue,
            onValueChange = onTitleChange
        )

        AddEventTextField(
            label = "Location",
            value = locationValue,
            onValueChange = onLocationChange
        )

        AddEventTextField(
            label = "Ticket price",
            value = ticketPriceValue,
            onValueChange = onTicketPriceChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        AddEventTextField(
            label = "Registration fee for artist",
            value = registrationFee,
            onValueChange = onRegistrationFeeChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
    }
}

@Preview
@Composable
fun AddEventFormPreview() {
    AddEventForm(
        titleValue = "",
        onTitleChange = {},
        locationValue = "",
        onLocationChange = {},
        ticketPriceValue = "",
        onTicketPriceChange = {},
        registrationFee = "",
        onRegistrationFeeChange = {}
    )
}
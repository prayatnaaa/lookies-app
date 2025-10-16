package com.prayatna.lookiesapp.presentation.components.addevent

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.ui.theme.DarkGrey
import com.prayatna.lookiesapp.ui.theme.Grey
import com.prayatna.lookiesapp.ui.theme.PureWhite
import io.ktor.websocket.Frame
import java.util.Calendar

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


@SuppressLint("DefaultLocale")
@Composable
fun AddEventDateTimeField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val formatted = String.format("%04d-%02d-%02d %02d:%02d", year, month + 1, dayOfMonth, hourOfDay, minute)
            onValueChange(formatted)
        }

        TimePickerDialog(
            context,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    Column (
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
                color = Grey
            )
        )
        OutlinedTextField(
            value = value,
            onValueChange = {},
            label = { Frame.Text(label) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PureWhite,
                focusedContainerColor = DarkGrey,
                focusedLabelColor = PureWhite,
                cursorColor = PureWhite,
                focusedTrailingIconColor = PureWhite,
                focusedTextColor = PureWhite,
                unfocusedTextColor = PureWhite
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    DatePickerDialog(
                        context,
                        dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
            readOnly = true,
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
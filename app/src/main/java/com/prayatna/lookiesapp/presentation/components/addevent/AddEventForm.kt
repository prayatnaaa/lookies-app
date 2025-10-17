package com.prayatna.lookiesapp.presentation.components.addevent

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.ui.theme.Grey
import com.prayatna.lookiesapp.ui.theme.PureWhite
import java.util.Calendar

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
    onRegistrationFeeChange: (String) -> Unit,
    dateValue: String,
    onDateChange: (String) -> Unit
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

        AddEventDateTimeField(
            label = "Date",
            value = dateValue,
            onValueChange = onDateChange
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
    var isFocused by remember { mutableStateOf(false) }

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

    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
                color = if (isFocused) PureWhite else Grey
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent, RoundedCornerShape(8.dp))
                .clickable {
                    DatePickerDialog(
                        context,
                        dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
                .border(
                    width = if (isFocused) 2.dp else 1.dp,
                    color = if (isFocused) PureWhite else Grey,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
        ) {
            Text(
                text = value.ifEmpty { "" },
                color = if (value.isNotEmpty()) PureWhite else Grey,
                fontSize = 16.sp
            )
        }
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
        onRegistrationFeeChange = {},
        dateValue = "",
        onDateChange = {}
    )
}
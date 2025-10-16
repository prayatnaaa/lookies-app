package com.prayatna.lookiesapp.presentation.components.addevent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.ui.theme.Grey
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventDateRangePicker(
    modifier: Modifier = Modifier,
    label: String = "Event Date Range",
    startDate: String,
    endDate: String,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    val dateFormatter = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }

    val displayValue = if (startDate.isNotEmpty() && endDate.isNotEmpty())
        "$startDate → $endDate" else ""

    Column (
        modifier = modifier.padding(vertical = 8.dp)
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
            value = displayValue,
            onValueChange = {},
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { showPicker = true },
            readOnly = true,
        )
    }

    if (showPicker) {
        val dateRangePickerState = rememberDateRangePickerState()

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val start = dateRangePickerState.selectedStartDateMillis
                    val end = dateRangePickerState.selectedEndDateMillis

                    if (start != null && end != null) {
                        onStartDateChange(dateFormatter.format(Date(start)))
                        onEndDateChange(dateFormatter.format(Date(end)))
                    }
                    showPicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DateRangePicker(state = dateRangePickerState)
        }
    }
}

@Preview
@Composable
fun AddEventDateRangePickerPreview() {
    AddEventDateRangePicker(
        startDate = "",
        endDate = "",
        onStartDateChange = {},
        onEndDateChange = {}
    )
}

package com.prayatna.lookiesapp.presentation.components.addevent

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val dateRangePickerState = rememberDateRangePickerState()

    val dateFormatter = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }

    val displayValue = if (startDate.isNotEmpty() && endDate.isNotEmpty())
        "$startDate → $endDate" else ""

    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent, RoundedCornerShape(8.dp))
                .clickable {
                    showPicker = true
                }
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            Text(
                text = displayValue.ifEmpty { "" },
                color = if (displayValue.isNotEmpty()) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                fontSize = 16.sp
            )
        }
    }

    if (showPicker) {
        Log.d("DATEPICKER", "Show picker")
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

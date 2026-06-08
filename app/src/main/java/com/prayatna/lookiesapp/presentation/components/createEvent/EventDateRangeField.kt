package com.prayatna.lookiesapp.presentation.components.createEvent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.utils.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDateRangeField(
    startDate: String,
    endDate: String,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    label: String? = null
) {
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val startPickerState = rememberDatePickerState()
    val endPickerState = rememberDatePickerState()

    val dateFormatter = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
            value = startDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("Start") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showStartPicker = true }) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Pick start date"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
            value = endDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("End") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { showEndPicker = true }) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Pick end date"
                    )
                }
            }
        )
    }

    if (showStartPicker) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startPickerState.selectedDateMillis?.let { millis ->
                        onStartDateChange(
                            dateFormatter.format(Date(millis))
                        )
                    }
                    showStartPicker = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(
                colors = DatePickerDefaults.colors(
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    headlineContentColor = MaterialTheme.colorScheme.onSurface,
                    weekdayContentColor = MaterialTheme.colorScheme.onSurface,
                    subheadContentColor = MaterialTheme.colorScheme.onSurface,
                    yearContentColor = MaterialTheme.colorScheme.onSurface,
                    currentYearContentColor = MaterialTheme.colorScheme.onSurface,
                    selectedYearContentColor = MaterialTheme.colorScheme.onSurface,
                    dayContentColor = MaterialTheme.colorScheme.onSurface,
                    todayContentColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                state = startPickerState
            )
        }
    }

    if (showEndPicker) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    endPickerState.selectedDateMillis?.let { millis ->
                        onEndDateChange(
                            dateFormatter.format(Date(millis))
                        )
                    }
                    showEndPicker = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = endPickerState,
                colors = DatePickerDefaults.colors(
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    headlineContentColor = MaterialTheme.colorScheme.onSurface,
                    weekdayContentColor = MaterialTheme.colorScheme.onSurface,
                    subheadContentColor = MaterialTheme.colorScheme.onSurface,
                    yearContentColor = MaterialTheme.colorScheme.onSurface,
                    currentYearContentColor = MaterialTheme.colorScheme.onSurface,
                    selectedYearContentColor = MaterialTheme.colorScheme.onSurface,
                    dayContentColor = MaterialTheme.colorScheme.onSurface,
                    todayContentColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.surface,
                ),)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventDateRangeFieldPreview() {
    EventDateRangeField(
        startDate = "dsd",
        endDate = "dsd",
        onStartDateChange = {},
        onEndDateChange = {}
    )
}

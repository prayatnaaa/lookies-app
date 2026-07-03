package com.prayatna.lookiesapp.presentation.components.createEvent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.prayatna.lookiesapp.utils.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDateField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    val pickerState = rememberDatePickerState()
    val dateFormatter = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }

    OutlinedTextField(
        shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
        value = value,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = { showPicker = true }) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Pick $label"
                )
            }
        }
    )

    if (showPicker) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { millis ->
                        onValueChange(dateFormatter.format(Date(millis)))
                    }
                    showPicker = false
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
                state = pickerState
            )
        }
    }
}

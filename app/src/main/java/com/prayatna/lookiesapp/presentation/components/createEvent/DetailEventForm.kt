package com.prayatna.lookiesapp.presentation.components.createEvent

import android.net.Uri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.event.EventFormat
import com.prayatna.lookiesapp.domain.model.event.TEventType
import com.prayatna.lookiesapp.presentation.components.CustomDropdownField
import com.prayatna.lookiesapp.presentation.components.CustomTextField
import com.prayatna.lookiesapp.presentation.components.registerBusiness.FormSectionCard

@Composable
fun DetailEventForm(
    eventName: String,
    onEventNameChange: (String) -> Unit,
    imageUri: Uri? = null,
    imageUriString: String? = null,
    onImageClick: () -> Unit,
    startDate: String,
    onStartDateChange: (String) -> Unit,
    endDate: String,
    onEndDateChange: (String) -> Unit,
    eventTypes: List<TEventType>,
    selectedEventTypeId: String,
    onEventTypeChange: (String) -> Unit,
    eventFormats: List<EventFormat>,
    selectedEventFormatId: String,
    onEventFormatChange: (String) -> Unit,
) {
    FormSectionCard(
        title = "Event Details",
        icon = Icons.Default.Event
    ) {
        Text(
            text = "Fill in the core information for your event",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        EventBannerCard(
            onClick = onImageClick,
            imageUri = imageUri,
            imageUrl = imageUriString
        )

        CustomTextField(
            value = eventName,
            onValueChange = onEventNameChange,
            label = "Event title"
        )

        CustomDropdownField(
            label = "Event Type",
            items = eventTypes,
            selectedItem = eventTypes.find {
                it.id.toString() == selectedEventTypeId
            },
            itemLabel = { it.name },
            onItemSelected = {
                onEventTypeChange(it.id.toString())
            }
        )

        CustomDropdownField(
            label = "Event format",
            items = eventFormats,
            selectedItem = eventFormats.find {
                it.id.toString() == selectedEventFormatId
            },
            itemLabel = { it.name },
            onItemSelected = {
                onEventFormatChange(it.id.toString())
            }
        )

        EventDateRangeField(
            startDate = startDate,
            endDate = endDate,
            onStartDateChange = onStartDateChange,
            onEndDateChange = onEndDateChange
        )
    }
}
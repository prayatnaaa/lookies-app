package com.prayatna.lookiesapp.presentation.components.createEvent

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.domain.model.event.EventFormat
import com.prayatna.lookiesapp.domain.model.event.TEventType
import com.prayatna.lookiesapp.presentation.components.CustomDropdownField
import com.prayatna.lookiesapp.presentation.components.CustomTextField

@Composable
fun DetailEventForm(
    eventName: String,
    onEventNameChange: (String) -> Unit,
    imageUri: Uri? = null,
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
    onEventFormatChange: (String) -> Unit

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Event Detail",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(4.dp),
        )
        EventBannerCard(
            onClick = onImageClick,
            imageUri = imageUri
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

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}
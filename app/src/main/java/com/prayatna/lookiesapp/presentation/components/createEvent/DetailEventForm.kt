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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Preview(showBackground = true)
@Composable
fun DetailEventFormPreview() {
    DetailEventForm(
        eventName = "Today Art Festival",
        onEventNameChange = {},
        imageUri = null,
        onImageClick = {},
        startDate = "11-12-2026",
        onStartDateChange = {},
        endDate = "21-12-2026",
        onEndDateChange = {}
    )
}
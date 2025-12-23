package com.prayatna.lookiesapp.presentation.components.createEvent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun EventLocationForm(
    locationName: String,
    onLocationNameChange: (String) -> Unit,
    locationUrl: String,
    onLocationUrlChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Event location",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Please fill in the location information to create an event",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        CustomTextField(
            value = locationName,
            onValueChange = onLocationNameChange,
            label = "Location name"
        )
        Spacer(modifier = Modifier.height(4.dp))
        CustomTextField(
            value = locationUrl,
            onValueChange = onLocationUrlChange,
            label = "Location URL (Optional)"
        )
        HorizontalDivider(thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventLocationFormPreview() {
    EventLocationForm(
        locationName = "Tabanan",
        onLocationNameChange = {},
        locationUrl = "eee/vp,tabanan",
        onLocationUrlChange = {}
    )
}
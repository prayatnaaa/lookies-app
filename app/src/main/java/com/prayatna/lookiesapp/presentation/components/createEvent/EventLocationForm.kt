package com.prayatna.lookiesapp.presentation.components.createEvent

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.CustomTextField
import com.prayatna.lookiesapp.presentation.components.registerBusiness.FormSectionCard

@Composable
fun EventLocationForm(
    locationName: String,
    onLocationNameChange: (String) -> Unit,
    locationUrl: String,
    onLocationUrlChange: (String) -> Unit
) {
    FormSectionCard(
        title = "Event Location",
        icon = Icons.Default.LocationOn
    ) {
        Text(
            text = "Where will your event take place?",
            style = MaterialTheme.typography.bodySmall,
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
    }
}
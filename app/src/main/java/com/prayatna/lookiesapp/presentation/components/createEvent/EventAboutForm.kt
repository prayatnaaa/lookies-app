package com.prayatna.lookiesapp.presentation.components.createEvent

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.CustomTextField
import com.prayatna.lookiesapp.presentation.components.registerBusiness.FormSectionCard

@Composable
fun EventAboutForm(
    eventDescription: String,
    onEventDescriptionChange: (String) -> Unit
) {
    FormSectionCard(
        title = "About",
        icon = Icons.Default.Description
    ) {
        Text(
            text = "Describe your event to attract attendees",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        CustomTextField(
            textFieldModifier = Modifier.height(200.dp),
            value = eventDescription,
            onValueChange = onEventDescriptionChange,
            label = "Event description",
            singleLine = false,
            maxLines = 5
        )
    }
}
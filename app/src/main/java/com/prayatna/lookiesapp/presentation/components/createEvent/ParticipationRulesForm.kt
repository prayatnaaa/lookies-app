package com.prayatna.lookiesapp.presentation.components.createEvent

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.CustomTextField
import com.prayatna.lookiesapp.presentation.components.registerBusiness.FormSectionCard

@Composable
fun ParticipationRulesForm(
    isSelfExhibition: Boolean,
    maxPainting: String?,
    onMaxPaintingChange: (String?) -> Unit,
    maxPaintingPerArtist: String?,
    onMaxPaintingPerArtistChange: (String?) -> Unit
) {
    FormSectionCard(
        title = "Artworks Capacity",
        icon = Icons.Default.Groups
    ) {
        Text(
            text = "Set limits for artwork contributions",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        CustomTextField(
            value = maxPainting ?: "",
            onValueChange = { input ->
                if (input.all { it.isDigit() }) onMaxPaintingChange(input)
            },
            label = if (isSelfExhibition) "Total Painting Capacity" else "Maximum Total Paintings (Event)",
            keyboardType = KeyboardType.Number
        )

        if (!isSelfExhibition) {
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextField(
                value = maxPaintingPerArtist ?: "",
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) onMaxPaintingPerArtistChange(input)
                },
                label = "Max Painting Limit per Artist",
                keyboardType = KeyboardType.Number
            )
        }
    }
}

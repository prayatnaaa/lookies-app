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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.presentation.components.CustomTextField

@Composable
fun ParticipationRulesForm(
    isSelfExhibition: Boolean,
    maxParticipants: String?,
    onMaxParticipantsChange: (String) -> Unit,
    maxPainting: String?,
    onMaxPaintingChange: (String?) -> Unit,
    maxPaintingPerArtist: String?,
    onMaxPaintingPerArtistChange: (String?) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Participants & Artworks Info",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Set limits for artists and paintings",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!isSelfExhibition) {
            CustomTextField(
                value = maxParticipants ?: "",
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) onMaxParticipantsChange(input)
                },
                label = "Maximum Artists (Participants)",
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        CustomTextField(
            value = maxPainting ?: "",
            onValueChange = { input ->
                if (input.all { it.isDigit() }) onMaxPaintingChange(input)
            },
            label = if (isSelfExhibition) "Total Painting Capacity" else "Maximum Total Paintings (Event)",
            keyboardType = KeyboardType.Number
        )

        if (!isSelfExhibition) {
            Spacer(modifier = Modifier.height(12.dp))
            CustomTextField(
                value = maxPaintingPerArtist ?: "",
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) onMaxPaintingPerArtistChange(input)
                },
                label = "Max Painting Limit per Artist",
                keyboardType = KeyboardType.Number
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}
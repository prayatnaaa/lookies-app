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
    maxParticipants: String,
    onMaxParticipantsChange: (String) -> Unit,
    maxPainting: String,
    onMaxPaintingChange: (String) -> Unit,
    maxPaintingPerArtist: String,
    onMaxPaintingPerArtistChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Participants Info",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        CustomTextField(
            value = maxParticipants,
            onValueChange = {
                if (it.all {
                    isDigit ->
                        isDigit.isDigit()
                    }) onMaxParticipantsChange(it)
            },
            label = "Maximum participants",
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = Modifier.height(4.dp))
        CustomTextField(
            value = maxPainting,
            onValueChange = {
                if (it.all {
                    isDigit ->
                        isDigit.isDigit()
                    }) onMaxPaintingChange(it)
            },
            label = "Maximum painting"
        )
        Spacer(modifier = Modifier.height(4.dp))
        CustomTextField(
            value = maxPaintingPerArtist,
            onValueChange = {
                if (it.all {
                    isDigit ->
                        isDigit.isDigit()
                    }) onMaxPaintingPerArtistChange(it)
            },
            label = "Maximum painting per artist"
        )
        HorizontalDivider(thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}
package com.prayatna.lookiesapp.presentation.components.createEvent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.registerBusiness.FormSectionCard
import com.prayatna.lookiesapp.utils.Constants

@Composable
fun RevenueShareForm(
    isSelfExhibition: Boolean,
    isOnline: Boolean,
    paintingArtistPercent: Int,
    onPaintingArtistPercentChange: (Int) -> Unit,
    paintingEventPercent: Int,
    onPaintingEventPercentChange: (Int) -> Unit,
    paintingPlatformPercent: Int?,
    onPaintingPlatformPercentChange: (Int) -> Unit,
    ticketArtistPercent: Int?,
    onTicketArtistPercentChange: (Int) -> Unit,
    ticketEventPercent: Int?,
    onTicketEventPercentChange: (Int) -> Unit,
    ticketPlatformPercent: Int?,
    onTicketPlatformPercentChange: (Int) -> Unit,
) {
    FormSectionCard(
        title = "Revenue Sharing",
        icon = Icons.Default.PieChart
    ) {
        Text(
            text = "Define how revenue is split between parties. Each group must total 100%.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Painting Revenue Section ---
        // Logic: Always visible.
        // Platform is always involved.
        // If NOT self exhibition: Split between Artist, Event, and Platform.
        // If self exhibition: Split between Event and Platform.
        RevenueSubSection(
            title = "Painting Sales Revenue",
            artistPercent = if (!isSelfExhibition) paintingArtistPercent else 0,
            onArtistPercentChange = onPaintingArtistPercentChange,
            eventPercent = paintingEventPercent,
            onEventPercentChange = onPaintingEventPercentChange,
            platformPercent = paintingPlatformPercent ?: 0,
            onPlatformPercentChange = onPaintingPlatformPercentChange,
            showArtist = !isSelfExhibition,
            showPlatform = true
        )

        // --- Ticket Revenue Section ---
        // Logic: 
        // 1. If Online: Hidden (Tickets don't exist for online self-exhibition/open-call usually, or handled separately)
        // 2. If Self Exhibition but OFFLINE: Show split between Platform and Event.
        // 3. If Open Call (Not Self Exhibition): Show split between Platform and Event.
        AnimatedVisibility(
            visible = !isOnline,
            enter = fadeIn(tween(300)) + expandVertically(tween(300)),
            exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                RevenueSubSection(
                    title = "Ticket Sales Revenue",
                    artistPercent = 0, // Tickets never split with artist in this context
                    onArtistPercentChange = {},
                    eventPercent = ticketEventPercent ?: 0,
                    onEventPercentChange = onTicketEventPercentChange,
                    platformPercent = ticketPlatformPercent ?: 0,
                    onPlatformPercentChange = onTicketPlatformPercentChange,
                    showArtist = false,
                    showPlatform = true
                )
            }
        }
    }
}

@Composable
private fun RevenueSubSection(
    title: String,
    artistPercent: Int,
    onArtistPercentChange: (Int) -> Unit,
    eventPercent: Int,
    onEventPercentChange: (Int) -> Unit,
    platformPercent: Int,
    onPlatformPercentChange: (Int) -> Unit,
    showArtist: Boolean = true,
    showPlatform: Boolean = true,
) {
    val total =
        (if (showArtist) artistPercent else 0) +
                eventPercent +
                (if (showPlatform) platformPercent else 0)

    val remaining = 100 - total
    val isValid = total == 100

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = if (isValid) "$total% ✓" else "$total%",
                color = if (isValid) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                fontWeight = FontWeight.Bold
            )
        }

        if (showArtist) {
            PercentInputRow(
                label = "Artist",
                value = artistPercent,
                onValueChange = onArtistPercentChange
            )
        }

        PercentInputRow(
            label = "Event",
            value = eventPercent,
            onValueChange = onEventPercentChange
        )

        if (showPlatform) {
            PercentInputRow(
                label = "Platform",
                value = platformPercent,
                onValueChange = onPlatformPercentChange
            )
        }

        if (!isValid) {
            Text(
                text = if (remaining > 0) {
                    "$remaining% remaining"
                } else {
                    "${-remaining}% over limit"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun PercentInputRow(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = label,
            modifier = Modifier.width(72.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        OutlinedTextField(
            value = value.toString(),
            onValueChange = { input ->
                val number = input.toIntOrNull()

                if (number != null && number in 0..100) {
                    onValueChange(number)
                } else if (input.isEmpty()) {
                    onValueChange(0)
                }
            },
            modifier = Modifier.weight(1f),
            singleLine = true,
            shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
            suffix = {
                Text("%")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }
}

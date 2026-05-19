package com.prayatna.lookiesapp.presentation.components.createEvent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.registerBusiness.FormSectionCard

@Composable
fun RevenueShareForm(
    isSelfExhibition: Boolean,
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
        RevenueSubSection(
            title = "Painting Sales Revenue",
            artistPercent = paintingArtistPercent,
            onArtistPercentChange = onPaintingArtistPercentChange,
            eventPercent = paintingEventPercent,
            onEventPercentChange = onPaintingEventPercentChange,
            platformPercent = paintingPlatformPercent ?: 0,
            onPlatformPercentChange = onPaintingPlatformPercentChange,
            showArtist = true,
            showPlatform = false

        )

        // --- Ticket Revenue Section (conditionally shown) ---
        AnimatedVisibility(
            visible = !isSelfExhibition,
            enter = fadeIn(tween(300)) + expandVertically(tween(300)),
            exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                RevenueSubSection(
                    title = "Ticket Sales Revenue",
                    artistPercent = ticketArtistPercent ?: 0,
                    onArtistPercentChange = onTicketArtistPercentChange,
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
            suffix = {
                Text("%")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }
}

@Composable
private fun DistributionBar(
    artistPercent: Int,
    eventPercent: Int,
    platformPercent: Int,
    artistColor: Color,
    eventColor: Color,
    platformColor: Color
) {
    val total = (artistPercent + eventPercent + platformPercent).coerceAtLeast(1)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(5.dp))
    ) {
        if (artistPercent > 0) {
            Box(
                modifier = Modifier
                    .weight(artistPercent.toFloat() / total)
                    .height(10.dp)
                    .background(artistColor)
            )
        }
        if (eventPercent > 0) {
            Box(
                modifier = Modifier
                    .weight(eventPercent.toFloat() / total)
                    .height(10.dp)
                    .background(eventColor)
            )
        }
        if (platformPercent > 0) {
            Box(
                modifier = Modifier
                    .weight(platformPercent.toFloat() / total)
                    .height(10.dp)
                    .background(platformColor)
            )
        }
        // Fill remaining with a subtle gray if total < 100
        if (total < 100) {
            Box(
                modifier = Modifier
                    .weight((100 - total).toFloat() / 100f)
                    .height(10.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            )
        }
    }

    // Legend
    Spacer(modifier = Modifier.height(4.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LegendItem(color = artistColor, label = "Artist")
        LegendItem(color = eventColor, label = "Event")
        LegendItem(color = platformColor, label = "Platform")
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PercentSliderRow(
    label: String,
    percent: Int,
    onPercentChange: (Int) -> Unit,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$percent%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Slider(
            value = percent.toFloat(),
            onValueChange = { onPercentChange(it.toInt()) },
            valueRange = 0f..100f,
            steps = 19, // 5% increments (0, 5, 10, ..., 100)
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color,
                inactiveTrackColor = color.copy(alpha = 0.2f)
            )
        )
    }
}

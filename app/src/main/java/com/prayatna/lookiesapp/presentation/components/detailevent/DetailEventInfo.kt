package com.prayatna.lookiesapp.presentation.components.detailevent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.domain.model.DetailEvent
import com.prayatna.lookiesapp.domain.model.Event
import com.prayatna.lookiesapp.presentation.components.eventlist.EventStatusText
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.utils.Helper

@Composable
fun DetailEventInfo(
    event: Event,
    detailEvent: DetailEvent,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            event.status?.let {
                EventStatusText(
                    status = it,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(
                modifier = Modifier.padding(vertical = 4.dp),
            )

            EventInfoRow(icon = Icons.Default.LocationOn, value = event.location)
//            EventInfoRow(label = "Date", value = event.date)
            EventInfoRow(
                icon = Icons.Default.DateRange,
                value = Helper.parseDateRange("${detailEvent.startTime} - ${detailEvent.endTime}")
            )
            EventInfoRow(
                icon = Icons.Default.Bookmarks,
                value = "${detailEvent.ticketQuantity} available"
            )
//            EventInfoRow(
//                label = "Registration Fee",
//                value = if (event.registrationFee == 0.0) "Free" else "Rp ${event.registrationFee}"
//            )

            Spacer(
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun EventInfoRow(
    icon: ImageVector,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BlackCharcoal,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
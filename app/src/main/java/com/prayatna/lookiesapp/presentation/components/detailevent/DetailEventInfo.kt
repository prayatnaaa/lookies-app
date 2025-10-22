package com.prayatna.lookiesapp.presentation.components.detailevent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.data.model.DetailEvent
import com.prayatna.lookiesapp.data.model.Event
import com.prayatna.lookiesapp.presentation.components.eventlist.EventStatusText

@Composable
fun DetailEventInfo(
    event: Event,
    detailEvent: DetailEvent,
    modifier: Modifier = Modifier
) {
    Card(
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
                    fontSize = 22.sp
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
                modifier = Modifier.padding(vertical = 8.dp),
            )

            InfoSectionTitle()
            EventInfoRow(label = "Location", value = event.location)
            EventInfoRow(label = "Date", value = event.date)
            EventInfoRow(label = "Time", value = "${detailEvent.startTime} - ${detailEvent.endTime}")
            EventInfoRow(label = "Tickets", value = "${detailEvent.ticketQuantity} available")
            EventInfoRow(
                label = "Ticket Price",
                value = if (event.ticketPrice == 0.0) "Free" else "Rp ${event.ticketPrice}"
            )
            EventInfoRow(
                label = "Registration Fee",
                value = if (event.registrationFee == 0.0) "Free" else "Rp ${event.registrationFee}"
            )

            Spacer(
                modifier = Modifier.padding(vertical = 8.dp),
            )

            Text(
                text = "View Location",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .align(Alignment.End)
            )
        }
    }
}

@Composable
private fun InfoSectionTitle() {
    Text(
        text = "Event Detail",
        style = MaterialTheme.typography.titleSmall.copy(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        ),
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun EventInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
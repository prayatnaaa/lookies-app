package com.prayatna.lookiesapp.presentation.components.eventlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.Event

@Composable
fun EventCard(
    modifier: Modifier = Modifier,
    event: Event,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            AsyncImage(
                model = event.bannerImageUrl,
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = modifier.height(4.dp))

            Row (
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = modifier.height(4.dp))

                    Text(
                        text = event.date,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = modifier.height(4.dp))

                    Text(
                        text = event.location,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = modifier.height(4.dp))

                    Text(
                        text = "Ticket Price: ${event.ticketPrice}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                EventStatusText(status = event.status.toString())
            }
        }
    }
}
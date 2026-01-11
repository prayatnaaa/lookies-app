package com.prayatna.lookiesapp.presentation.components.eventlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.utils.formatRupiah

@Composable
fun EventCard(
    modifier: Modifier = Modifier,
    event: Event,
    showTicketPrice: Boolean = true,
    showStatus: Boolean = false,
    actions: (@Composable RowScope.() -> Unit)? = null,
    onClick: () -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column {
            AsyncImage(
                model = event.bannerImageUrl
                    .replace("http://172.21.179.110", "http://10.0.2.2"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = event.startDate)

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = event.location)

                    if (showTicketPrice) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatRupiah(event.ticketPrice ?: 0.0),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    if (showStatus) {
                        EventStatusText(status = event.status)
                    }

                    if (actions != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            content = actions
                        )
                    }
                }
            }
        }
    }
}
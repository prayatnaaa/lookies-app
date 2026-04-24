package com.prayatna.lookiesapp.presentation.exhibitionHistory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.EventPaintingDetailViewModel
import com.prayatna.lookiesapp.utils.NavigationRoutes
import com.prayatna.lookiesapp.utils.formatRupiah

/** Statuses where the artist has a shipment action to take. */
private val SHIPMENT_ACTIONABLE_STATUSES = setOf(
    "accepted",            // artist submits inbound shipment
    "shipping_to_event",   // waiting for organizer confirmation (view only)
    "exhibited",           // artwork in gallery (view only)
    "unsold",              // organizer will create return — view only for artist
    "returning_to_creator",// artist confirms receipt
)

@Composable
fun ArtistExhibitionPaintingDetailScreen(
    navController: NavController,
    eventPaintingId: String,
    viewModel: EventPaintingDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(eventPaintingId) {
        viewModel.getEventPaintingDetail(eventPaintingId)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BackTopBar(
                onBackClick = { navController.popBackStack() },
                title = "Artwork Detail"
            )
        },
        bottomBar = {
            state.data?.let { painting ->
                if (painting.status in SHIPMENT_ACTIONABLE_STATUSES) {
                    ShipmentActionBar(
                        status = painting.status,
                        onManageShipment = {
                            navController.navigate(
                                NavigationRoutes.EXHIBITION_SHIPMENT + "/${painting.id}"
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularLoading()
            }
            return@Scaffold
        }

        state.data?.let { painting ->
            ArtistPaintingDetailContent(
                painting = painting,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun ArtistPaintingDetailContent(
    painting: EventPainting,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Painting image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = painting.painting.paintingUrl.replace(
                    "http://172.21.179.110", "http://10.0.2.2"
                ),
                contentDescription = painting.painting.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 240.dp, max = 480.dp)
            )
        }

        Column(modifier = Modifier.padding(20.dp)) {
            // Title + status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = painting.painting.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                EventPaintingStatusBadge(status = painting.status)
            }

            Spacer(Modifier.height(4.dp))
            Text(
                text = formatRupiah(painting.finalPrice),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Event info card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Event",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = painting.participant.event.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "by ${painting.participant.event.organizer.legalName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        LabelValue("Start", painting.participant.event.startDate.take(10))
                        LabelValue("End", painting.participant.event.endDate.take(10))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Artist info
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = painting.participant.artist.profileUrl
                        ?: "https://ui-avatars.com/api/?name=${painting.participant.artist.username}",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = painting.participant.artist.fullName
                            ?: painting.participant.artist.username ?: "Unknown",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Artist",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(100.dp))
        }
    }
}

@Composable
private fun LabelValue(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ShipmentActionBar(
    status: String,
    onManageShipment: () -> Unit
) {
    val (label, enabled) = when (status.lowercase()) {
        "approved" -> "Submit Inbound Shipment" to true
        "shipping_to_event" -> "Shipment In Progress…" to false
        "exhibited" -> "Artwork Exhibited" to false
        "unsold" -> "Return Being Arranged" to false
        "returning_to_creator" -> "Confirm Artwork Received" to true
        else -> "Manage Shipment" to true
    }

    Surface(
        shadowElevation = 16.dp,
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocalShipping,
                contentDescription = null,
                tint = if (enabled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(24.dp)
            )
            Button(
                onClick = onManageShipment,
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(text = label, fontWeight = FontWeight.Bold)
            }
        }
    }
}

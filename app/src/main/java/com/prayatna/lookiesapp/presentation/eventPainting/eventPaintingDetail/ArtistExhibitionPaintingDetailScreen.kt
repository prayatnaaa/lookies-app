package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.eventPainting.ArtistShipmentActionBar
import com.prayatna.lookiesapp.presentation.components.eventPainting.EventPaintingDetailContent
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.utils.NavigationRoutes

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
                when {
                    painting.status.lowercase() == "rejected" -> {
                        ArtistResubmitActionBar(
                            onResubmit = {
                                val eventId = painting.participant.event.id
                                val merchantId = painting.participant.event.organizer.id
                                navController.navigate(
                                    "${NavigationRoutes.INSERT_EVENT_PAINTINGS_ROUTE}/$eventId/$merchantId"
                                )
                            }
                        )
                    }
                    painting.status in SHIPMENT_ACTIONABLE_STATUSES -> {
                        ArtistShipmentActionBar(
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
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularLoading()
            }
            return@Scaffold
        }

        state.data?.let { painting ->
            EventPaintingDetailContent(
                painting = painting,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun ArtistResubmitActionBar(
    onResubmit: () -> Unit
) {
    Surface(
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your painting was rejected.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = onResubmit) {
                Text(
                    text = "Edit & Resubmit",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
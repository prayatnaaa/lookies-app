package com.prayatna.lookiesapp.presentation.partner.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.partner.PartnerDashboard
import com.prayatna.lookiesapp.presentation.components.partner.DashboardActionItem
import com.prayatna.lookiesapp.presentation.components.partner.StatCard
import com.prayatna.lookiesapp.presentation.partner.main.home.state.PartnerHomeEvent
import com.prayatna.lookiesapp.presentation.partner.main.home.state.PartnerHomeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerHomeScreen(
    state: PartnerHomeUiState,
    onEvent: (PartnerHomeEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent(PartnerHomeEvent.BackClicked)
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                title = {
                    Column {
                        Text(
                            text = state.profile?.tradingName
                                ?: state.profile?.legalName
                                ?: "Partner Dashboard",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "Manage your business",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },

                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onEvent(PartnerHomeEvent.CreateEventClicked)
                },
                icon = {
                    Icon(Icons.Default.Add, null)
                },
                text = {
                    Text("Create Event")
                },
                shape = CircleShape
            )
        },

        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            when {

                state.isLoading && state.profile == null -> {
                    LoadingContent()
                }

                state.errorMessage != null &&
                        state.profile == null -> {
                    ErrorContent(
                        message = state.errorMessage,
                        onRetry = {
                            onEvent(PartnerHomeEvent.Retry)
                        }
                    )
                }

                else -> {
                    DashboardContent(
                        state = state,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardContent(
    state: PartnerHomeUiState,
    onEvent: (PartnerHomeEvent) -> Unit
) {
    val profile = state.profile ?: return

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        item {
            WelcomeHeader(
                name = profile.tradingName ?: profile.legalName,
                pictureUrl = profile.pictureUrl,
                description = profile.description
            )
        }

        item {
            SectionTitle("Business Status")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Status",
                    value = profile.kycStatus.replaceFirstChar {
                        it.uppercase()
                    },
                    icon = Icons.Outlined.Shield
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Payout",
                    value = if (profile.payoutEnabled)
                        "Enabled"
                    else
                        "Disabled",
                    icon = if (profile.payoutEnabled)
                        Icons.Default.CheckCircle
                    else
                        Icons.Default.WarningAmber
                )
            }
        }

        item {
            SectionTitle("Event Overview")

            if (state.dashboard != null) {
                EventStats(state.dashboard)
            } else {
                StatsLoading()
            }
        }

        item {
            ManagementSection(onEvent)
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun EventStats(
    dashboard: PartnerDashboard
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        StatCard(
            modifier = Modifier.weight(1f),
            title = "Active Events",
            value = dashboard.activeEvents.toString(),
            icon = Icons.Outlined.EventAvailable
        )

        StatCard(
            modifier = Modifier.weight(1f),
            title = "Tickets Sold",
            value = dashboard.totalTicketsSold.toString(),
            icon = Icons.Outlined.ConfirmationNumber
        )
    }
}

@Composable
private fun StatsLoading() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Spacer(modifier = Modifier.height(8.dp))

        Text("Loading stats...")
    }
}

@Composable
private fun ManagementSection(
    onEvent: (PartnerHomeEvent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        SectionTitle("Management")

        DashboardActionItem(
            title = "My Events",
            subtitle = "View and manage your events",
            icon = Icons.Default.Event,
            onClick = {
                onEvent(PartnerHomeEvent.MyEventsClicked)
            }
        )

        DashboardActionItem(
            title = "Refunds",
            subtitle = "Manage refund requests from buyer",
            icon = Icons.Default.Group,
            onClick = {
                onEvent(PartnerHomeEvent.RefundClicked)
            }
        )

        DashboardActionItem(
            title = "Paintings",
            subtitle = "Manage paintings collection",
            icon = Icons.Default.Group,
            onClick = {
                onEvent(PartnerHomeEvent.PaintingClicked)
            }
        )

        DashboardActionItem(
            title = "Shipments",
            subtitle = "Manage shipments of customers",
            icon = Icons.Default.Group,
            onClick = {
                onEvent(PartnerHomeEvent.ShipmentClicked)
            }
        )
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Loading dashboard...")
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            Icons.Default.WarningAmber,
            null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Failed to load dashboard",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = message)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Icon(Icons.Default.Refresh, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Retry")
        }
    }
}

@Composable
private fun SectionTitle(
    title: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun WelcomeHeader(
    name: String,
    pictureUrl: String?,
    description: String?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            )
            .padding(20.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = pictureUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        Color.White.copy(alpha = 0.2f)
                    )
                    .border(
                        2.dp,
                        Color.White.copy(alpha = 0.5f),
                        CircleShape
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Welcome back,",
                    color = Color.White.copy(alpha = 0.8f)
                )

                Text(
                    text = name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = description,
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
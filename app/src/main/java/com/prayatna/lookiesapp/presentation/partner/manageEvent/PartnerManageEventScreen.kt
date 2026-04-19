package com.prayatna.lookiesapp.presentation.partner.manageEvent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.data.remote.dto.EventStatisticDto
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.eventlist.EventCard
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.utils.NavigationRoutes
import com.prayatna.lookiesapp.utils.formatRupiah

@Composable
fun PartnerManageEventScreen(
    navController: NavController,
    eventId: String,
    viewModel: PartnerManageEventViewModel = hiltViewModel()
) {
    LaunchedEffect(eventId) {
        viewModel.getEvent(eventId)
        viewModel.getEventStatistic(eventId)
    }

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BackTopBar(
                onBackClick = {
                    navController.popBackStack()
                },
                title = "Manage Event",
            )
        }
    ) { innerPadding ->

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularLoading()
            }
        } else if (uiState.errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text(text = uiState.errorMessage ?: "Unknown Error", color = MaterialTheme.colorScheme.error)
            }
        } else {
            uiState.event?.let { event ->
                val isSelfExhibition = remember(event.eventType) { event.eventType.slug == "self_exhibition" }

                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        EventCard(
                            event = event,
                            showStatus = true,
                            onClick = {}
                        )
                    }

                    uiState.statistics?.let { stats ->
                        item {
                            EventStatisticsDashboard(stats, isSelfExhibition)
                        }
                    }

                    item {
                        Text(
                            text = "Management Menu",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    if (!isSelfExhibition) {
                        item {
                            ManagementOptionCard(
                                title = "Participants / Artists",
                                description = "Approve pendaftar, lihat list seniman, dan status keikutsertaan.",
                                icon = Icons.Default.Group,
                                alertCount = uiState.statistics?.pendingParticipants?.toInt() ?: 0,
                                onClick = {
                                    navController.navigate("${NavigationRoutes.EVENT_PAINTING_LIST}/${event.id}")
                                }
                            )
                        }
                    }

                    item {
                        ManagementOptionCard(
                            title = "Artworks & Gallery",
                            description = "Lihat semua lukisan yang masuk, kurasi lukisan, dan atur display.",
                            icon = Icons.Default.Palette,
                            onClick = {
                                navController.navigate("${NavigationRoutes.EVENT_PAINTING_LIST}/${event.id}?eventType=${event.eventType.slug}&businessId=${event.organizer.id}")
                            }
                        )
                    }

                    item {
                        ManagementOptionCard(
                            title = "Edit Event Details",
                            description = "Ubah deskripsi, banner, tanggal, atau informasi lokasi.",
                            icon = Icons.Default.Edit,
                            onClick = {
                                navController.navigate("${NavigationRoutes.EDIT_EVENT}/${event.id}")
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EventStatisticsDashboard(
    stats: EventStatisticDto,
    isSelfExhibition: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Total Revenue",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = formatRupiah(stats.totalRevenue),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatGroup(
                        modifier = Modifier.weight(1f),
                        title = "Tickets",
                        count = stats.ticketsSold.toString(),
                        revenue = formatRupiah(stats.ticketRevenue),
                        icon = Icons.Default.ConfirmationNumber,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    StatGroup(
                        modifier = Modifier.weight(1f),
                        title = "Paintings Sold",
                        count = stats.paintingsSold.toString(),
                        revenue = formatRupiah(stats.paintingRevenue),
                        icon = Icons.Default.ShoppingBag,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatBoxSimple(
                        label = "Total Artworks",
                        value = stats.totalPaintings.toString(),
                        icon = Icons.Default.Palette,
                        modifier = Modifier.weight(1f)
                    )

                    if (!isSelfExhibition) {
                        StatBoxSimple(
                            label = "Artists",
                            value = "${stats.approvedArtists}/${stats.approvedArtists + stats.pendingParticipants}",
                            icon = Icons.Default.Group,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        StatBoxSimple(
                            label = "Avg. Price",
                            value = if (stats.paintingsSold > 0) formatRupiah(stats.paintingRevenue / stats.paintingsSold) else "-",
                            icon = Icons.Default.Analytics,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatGroup(
    modifier: Modifier = Modifier,
    title: String,
    count: String,
    revenue: String,
    icon: ImageVector,
    color: Color
) {
    Column(
        modifier = modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = count, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(text = revenue, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun StatBoxSimple(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            Text(text = value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ManagementOptionCard(
    title: String,
    description: String,
    icon: ImageVector,
    alertCount: Int = 0,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (alertCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.error,
                            shape = CircleShape,
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = alertCount.toString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}
package com.prayatna.lookiesapp.presentation.partner.manageEvent

import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.eventlist.EventCard
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.utils.DateHelper
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun PartnerManageEventScreen(
    navController: NavController,
    eventId: String,
    viewModel: PartnerManageEventViewModel = hiltViewModel()
) {
    LaunchedEffect(eventId) {
        viewModel.getEvent(eventId)
    }

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    // 1. Handle Loading State
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularLoading() // Pastikan komponen ini ada
        }
        return // Stop rendering ke bawah jika loading
    }

    // 2. Handle Data Ready (Unwrap Nullable Event)
    uiState.event?.let { event ->

        // Logic Self Exhibition dipindahkan ke sini agar aman dari null
        val isSelfExhibition = remember(event.eventType) { event.eventType.slug == "self_exhibition" }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                BackTopBar(
                    navController = navController,
                    title = "Manage Event",
//                    actions = {
//                        // Tombol Preview sangat berguna untuk Partner melihat hasil jadinya
//                        IconButton(onClick = {
//                            navController.navigate("${NavigationRoutes.DETAIL_EVENT}/${event.id}")
//                        }) {
//                            Icon(Icons.Default.Visibility, contentDescription = "Preview")
//                        }
//                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // --- 1. HEADER RINGKAS ---
                item {
                    EventCard(
                        event = event,
                        showStatus = true,
                        onClick = {}
                    )
                }

                // --- 2. MENU MANAJEMEN UTAMA ---
                item {
                    Text(
                        text = "Management Menu",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // Menu: Kelola Peserta / Seniman (Hanya jika Group Exhibition)
                if (!isSelfExhibition) {
                    item {
                        ManagementOptionCard(
                            title = "Participants / Artists",
                            description = "Approve pendaftar, lihat list seniman, dan status keikutsertaan.",
                            icon = Icons.Default.Group,
                            alertCount = 0,
                            onClick = {
                                navController.navigate("${NavigationRoutes.PARTICIPANT_LIST}/${event.id}")
                            }
                        )
                    }
                }

                // Menu: Kelola Lukisan (Artworks)
                item {
                    ManagementOptionCard(
                        title = "Artworks & Gallery",
                        description = "Lihat semua lukisan yang masuk, kurasi lukisan, dan atur display.",
                        icon = Icons.Default.Palette,
                        onClick = {
                            if (isSelfExhibition) {
                                navController.navigate("${NavigationRoutes.MANAGE_PAINTINGS}/${event.id}")
                            } else {
                                navController.navigate("${NavigationRoutes.EVENT_PAINTING_LIST}/${event.id}")
                            }
                        }
                    )
                }

                // Menu: Edit Event
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
                    QuickStatsGrid(event = event)
                }
            }
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Event not found or failed to load.")
        }
    }
}



@Composable
fun EventSummaryCard(event: Event) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Banner Image
            AsyncImage(
                model = event.bannerImageUrl.replace("http://172.21.179.110", "http://10.0.2.2"),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // Status Chip
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = event.status, // "Approved" / "Open"
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${DateHelper.formatDate(event.startDate)} - ${DateHelper.formatDate(event.endDate)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
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
            // Icon Box
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
                    // Notification Badge (Misal ada 3 pendaftar baru)
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

@Composable
fun QuickStatsGrid(event: Event) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        // Stat 1: Kapasitas Lukisan
        QuickStatItem(
            label = "Capacity",
            value = "${event.maxPainting}",
            subLabel = "Paintings",
            modifier = Modifier.weight(1f)
        )
        // Stat 2: Kapasitas Artis
        if (event.maxParticipant != null) {
            QuickStatItem(
                label = "Quota",
                value = "${event.maxParticipant}",
                subLabel = "Artists",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun QuickStatItem(label: String, value: String, subLabel: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(text = subLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
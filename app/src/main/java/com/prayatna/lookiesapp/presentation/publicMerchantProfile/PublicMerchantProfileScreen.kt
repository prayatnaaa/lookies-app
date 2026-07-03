package com.prayatna.lookiesapp.presentation.publicMerchantProfile

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBusiness
import com.prayatna.lookiesapp.presentation.components.CustomAsyncImage
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.publicMerchantProfile.state.PublicMerchantProfileEvent
import com.prayatna.lookiesapp.presentation.publicMerchantProfile.state.PublicMerchantProfileUiState
import com.prayatna.lookiesapp.utils.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicMerchantProfileScreen(
    uiState: PublicMerchantProfileUiState,
    onEvent: (PublicMerchantProfileEvent) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = { onEvent(PublicMerchantProfileEvent.OnBackClicked) },
                title = "Organizer Details"
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading && uiState.merchant == null) {
                CircularLoading(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.errorMessage != null && uiState.merchant == null) {
                Text(
                    text = uiState.errorMessage,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    uiState.merchant?.let { merchant ->
                        item {
                            MerchantHeader(merchant = merchant)
                        }

                        item {
                            Column(
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                // ── Quick Stats ──────────────────────────────
                                ProfileStatsRow(eventCount = uiState.ongoingEvents.size)

                                // ── About Section ────────────────────────────
                                if (merchant.description?.isNotBlank() == true) {
                                    InfoSection(title = "About Organizer") {
                                        Text(
                                            text = merchant.description,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 22.sp
                                        )
                                    }
                                }

                                // ── Business Info ──────────────────────────
                                InfoSection(title = "Business Information") {
                                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        DetailRow(
                                            icon = Icons.Default.Business,
                                            label = "Type",
                                            value = merchant.merchantType.replace("_", " ").uppercase()
                                        )
                                        DetailRow(
                                            icon = Icons.Default.Public,
                                            label = "Region",
                                            value = merchant.countryOfOperation
                                        )
                                        if (merchant.tradingName != null) {
                                            DetailRow(
                                                icon = Icons.Default.Badge,
                                                label = "Trading Name",
                                                value = merchant.tradingName
                                            )
                                        }
                                    }
                                }

                                // ── Contact & Links ──────────────────────────
                                if (merchant.websiteUrl != null || merchant.email != null) {
                                    InfoSection(title = "Contact & Links") {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            merchant.websiteUrl?.let { url ->
                                                ContactButton(
                                                    icon = Icons.Default.Language,
                                                    label = "Website",
                                                    onClick = {
                                                        val formattedUrl =
                                                            if (url.startsWith("http")) {
                                                                url
                                                            } else {
                                                                "https://$url"
                                                            }

                                                        val intent = Intent(
                                                            Intent.ACTION_VIEW,
                                                            formattedUrl.toUri()
                                                        )

                                                        context.startActivity(intent)
                                                    },
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                            merchant.email?.let { email ->
                                                ContactButton(
                                                    icon = Icons.Default.Email,
                                                    label = "Email",
                                                    onClick = {
                                                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                                                            data = "mailto:$email".toUri()
                                                        }
                                                        context.startActivity(intent)
                                                    },
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                        }
                                    }
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                                // ── Ongoing Events ───────────────────────────
                                Text(
                                    text = "Ongoing Exhibitions",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (uiState.ongoingEvents.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "No active exhibitions at the moment.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(24.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        } else {
                            items(uiState.ongoingEvents) { event ->
                                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                                    OngoingEventRow(
                                        event = event,
                                        onClick = { onEvent(PublicMerchantProfileEvent.OnEventClicked(event.id)) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MerchantHeader(merchant: MerchantBusiness) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        // Simple background gradient or tint
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomAsyncImage(
                model = merchant.pictureUrl,
                contentDescription = merchant.legalName,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(2.dp)
                    .clip(CircleShape)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = merchant.legalName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = merchant.industryCategory.replace("_", " ").uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ProfileStatsRow(eventCount: Int) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(label = "Active Events", value = eventCount.toString(), icon = Icons.Default.Event)
            VerticalDivider(modifier = Modifier.height(30.dp), color = MaterialTheme.colorScheme.outlineVariant)
            StatItem(label = "Verified", value = "Yes", icon = Icons.Default.VerifiedUser)
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(6.dp))
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
        }
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun InfoSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 0.5.sp
        )
        content()
    }
}

@Composable
private fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(12.dp))
        Text(text = "$label:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(100.dp))
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ContactButton(icon: ImageVector, label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Icon(icon, null, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun OngoingEventRow(
    event: Event,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomAsyncImage(
                model = event.bannerImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = event.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${formatDate(event.startDate)} - ${formatDate(event.endDate)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

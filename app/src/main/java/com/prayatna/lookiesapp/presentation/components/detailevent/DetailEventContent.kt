package com.prayatna.lookiesapp.presentation.components.detailevent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.presentation.components.painting.PaintingCard
import com.prayatna.lookiesapp.utils.Constants
import com.prayatna.lookiesapp.utils.formatDate
import com.prayatna.lookiesapp.utils.formatRupiah

@Composable
fun DetailEventContent(
    modifier: Modifier = Modifier,
    event: Event,
    paintings: List<EventPainting> = emptyList(),
    showStatus: Boolean = false,
    isUserArtist: Boolean = false,
    onPartnerClick: (String) -> Unit = {},
    onPaintingClick: (String) -> Unit = {},
    extraContent: @Composable (() -> Unit)? = null
) {
    val isOnlineEvent = remember(event.eventFormat) { event.eventFormat.slug == "online" }
    val isSelfExhibition = remember(event.eventType) { event.eventType.slug == "self_exhibition" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Box {
                AsyncImage(
                    model = event.bannerImageUrl.replace("http://172.21.179.110", "http://10.0.2.2"),
                    contentDescription = "Banner Event ${event.title}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                if (showStatus) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatusChip(
                            label = event.status,
                            isOpen = event.status.equals("Open", true)
                        )

                        SuggestionChip(
                            onClick = {},
                            label = { Text(event.eventFormat.name) },
                            icon = {
                                Icon(
                                    if(isOnlineEvent) Icons.Default.Videocam else Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                            ),
                            border = null
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {

                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            onPartnerClick(event.organizer.id)
                        }
                        .fillMaxWidth()
                        .border(
                            width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE)
                        )
                        .padding(12.dp)
                ) {
                    AsyncImage(
                        model = event.organizer.pictureUrl
                            ?.replace("http://172.21.179.110", "http://10.0.2.2"),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                            .background(MaterialTheme.colorScheme.secondary),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Organized by",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = event.organizer.legalName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(modifier = Modifier
                    .fillMaxSize()) {
                    EventDetailItem(
                        icon = Icons.Default.DateRange,
                        label = "Date",
                        value = "${formatDate(event.startDate)} - ${formatDate(event.endDate)}",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    EventDetailItem(
                        icon = if (isOnlineEvent) Icons.Default.Videocam else Icons.Default.LocationOn,
                        label = if (isOnlineEvent) "This is" else "Location",
                        value =  if (isOnlineEvent) "Online" else event.location,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

                SectionTitle(title = "About Event")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = event.about ?: "No description provided by the organizer.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                extraContent?.invoke()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionTitle(title = "Featured Artworks")
                    if (paintings.isNotEmpty()) {
                        Text(
                            text = "${paintings.size} items",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (paintings.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(paintings.take(5)) { painting ->
                            PaintingCard(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(210.dp),
                                paintingUrl = painting.painting.paintingUrl,
                                name = painting.painting.title,
                                price = painting.finalPrice,
                                onClick = { onPaintingClick(painting.id) },
                                artistName = painting.participant.artist.fullName,
                                status = painting.status
                            )
                        }
                    }
                } else {
                    EmptyGalleryPlaceholder()
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (event.ticketPrice != null) {
                    val ticketLabel = if (isOnlineEvent) "Digital Access Fee" else "Visitor Ticket"
                    val ticketValue = if (event.ticketPrice == 0.0) "Free Entry" else formatRupiah(event.ticketPrice)

                    TicketInfoCard(label = ticketLabel, value = ticketValue)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                if (isUserArtist) {
                    ArtistRestrictedSection(
                        event = event,
                        isSelfExhibition = isSelfExhibition
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                Spacer(modifier = Modifier.height(80.dp)) // Bottom Padding
            }
        }
    }
}

// ==========================================
//              SUB-COMPONENTS
// ==========================================

@Composable
fun ArtistRestrictedSection(
    event: Event,
    isSelfExhibition: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Artist Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )

            // --- CAPACITY INFO ---
            Text(
                text = "Event Quota Limits",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CompactInfoCard(
                    title = "Paintings Quota",
                    value = event.remainingPaintingQuota.toString(),
                    icon = Icons.Default.Palette,
                    modifier = Modifier.weight(1f)
                )

                if (!isSelfExhibition) {
                    CompactInfoCard(
                        title = "Tickets Quota",
                        value = event.remainingParticipantQuota.toString(),
                        icon = Icons.Default.Person,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (!isSelfExhibition) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Your Limit: ${event.maxPaintingPerArtist} paintings per artist",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Registration Deadline: ${formatDate(event.paintingSubmissionDeadline ?: "2022-18-02")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!isSelfExhibition && event.artistRegistrationFee != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Participation Cost",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))

                val feeValue = if (event.artistRegistrationFee == 0.0) "Free" else formatRupiah(event.artistRegistrationFee)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Registration Fee", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = feeValue,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun TicketInfoCard(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ConfirmationNumber,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun StatusChip(label: String, isOpen: Boolean) {
    Surface(
        color = if (isOpen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
        shape = RoundedCornerShape(50),
        modifier = Modifier.height(32.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun EventDetailItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    subValue: String? = null,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )
            if (subValue != null) {
                Text(
                    text = subValue,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun CompactInfoCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun EmptyGalleryPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(12.dp)
            )
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No artworks on display yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
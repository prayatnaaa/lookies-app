package com.prayatna.lookiesapp.presentation.components.eventPainting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.presentation.exhibitionHistory.EventPaintingStatusBadge
import com.prayatna.lookiesapp.utils.formatRupiah

@Composable
fun EventPaintingDetailContent(
    painting: EventPainting,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {

        // Artwork image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = painting.painting.paintingUrl.replace(
                    "http://172.21.179.110",
                    "http://10.0.2.2"
                ),
                contentDescription = painting.painting.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 240.dp, max = 480.dp)
            )
        }

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = painting.painting.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = formatRupiah(painting.finalPrice),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                EventPaintingStatusBadge(
                    status = painting.status
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Rejection reason
            if (
                painting.status.lowercase() == "rejected" &&
                !painting.rejectionReason.isNullOrBlank()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFC62828),
                        modifier = Modifier.size(22.dp)
                    )

                    Column {
                        Text(
                            text = "Rejected by Partner",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = painting.rejectionReason,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFB71C1C)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Artwork Details
            InfoSection("Artwork Details") {
                DetailRow(
                    "Medium",
                    painting.painting.medium
                )

                DetailRow(
                    "Dimensions",
                    "${painting.painting.dimensionWidth} × " +
                            "${painting.painting.dimensionHeight} cm"
                )

                painting.painting.artStyle?.let {
                    DetailRow("Style", it)
                }

                painting.painting.subject?.let {
                    DetailRow("Subject", it)
                }

                DetailRow(
                    "Year Created",
                    painting.painting.yearCreated.toString()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoSection("Pricing") {
                DetailRow(
                    "Event Price",
                    formatRupiah(painting.finalPrice)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            if (painting.painting.description.isNotBlank()) {
                InfoSection("Description") {
                    Text(
                        text = painting.painting.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Event Information
            InfoSection("Event Information") {

                DetailRow(
                    "Event",
                    painting.participant.event.title
                )

                DetailRow(
                    "Organizer",
                    painting.participant.event.organizer.legalName
                )

                DetailRow(
                    "Start Date",
                    painting.participant.event.startDate.take(10)
                )

                DetailRow(
                    "End Date",
                    painting.participant.event.endDate.take(10)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Artist
            InfoSection("Artist") {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AsyncImage(
                        model =
                            painting.participant.artist.profileUrl
                                ?: "https://ui-avatars.com/api/?name=${
                                    painting.participant.artist.username
                                }",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant,
                                CircleShape
                            )
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant
                            )
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {

                        Text(
                            text = painting.participant.artist.fullName
                                ?: painting.participant.artist.username
                                ?: "Unknown Artist",
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Timeline
            InfoSection("Timeline") {

                DetailRow(
                    "Submitted",
                    painting.createdAt.take(10)
                )

                DetailRow(
                    "Status",
                    painting.status.replaceFirstChar {
                        it.uppercase()
                    }
                )
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
private fun InfoSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
                    .copy(alpha = 0.35f)
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            content()
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(10.dp))
    }
}
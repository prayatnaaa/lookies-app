package com.prayatna.lookiesapp.presentation.components.paintingSubmission

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.presentation.components.partner.StatusPill
import com.prayatna.lookiesapp.utils.formatRupiah

@Composable
fun PaintingSubmissionCard(
    item: EventPainting,
    isLoading: Boolean,
    onClick: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    val painting = item.painting
    var menuExpanded by remember { mutableStateOf(false) }

    val isActionEnabled = item.status.equals("f", ignoreCase = true)
    val isAccepted = item.status.equals("accepted", ignoreCase = true)
    val isRejected = item.status.equals("rejected", ignoreCase = true)

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .animateContentSize(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {

                AsyncImage(
                    model = painting.paintingUrl
                        .replace("http://172.21.179.110", "http://10.0.2.2"),
                    contentDescription = painting.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .aspectRatio(3f / 4f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        StatusPill(text = item.status)

                        Box {
                            IconButton(
                                enabled = !isLoading,
                                onClick = { menuExpanded = true }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.MoreVert,
                                    contentDescription = "More options",
                                    tint =
                                        if (isActionEnabled && !isLoading)
                                            MaterialTheme.colorScheme.onSurface
                                        else
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            }

                            DropdownMenu(
                                containerColor = MaterialTheme.colorScheme.surface,
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {

                                if (!isAccepted) {
                                    DropdownMenuItem(
                                        text = { Text("Approve") },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Outlined.CheckCircle,
                                                contentDescription = null,
                                                tint = Color(0xFF2E7D32)
                                            )
                                        },
                                        onClick = {
                                            menuExpanded = false
                                            onApprove()
                                        }
                                    )
                                }

                                if (!isRejected) {
                                    DropdownMenuItem(
                                        text = { Text("Reject") },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Cancel,
                                                contentDescription = null,
                                                tint = Color(0xFFC62828)
                                            )
                                        },
                                        onClick = {
                                            menuExpanded = false
                                            onReject()
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = painting.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = formatRupiah(item.finalPrice),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Crossfade(targetState = isLoading) { loading ->
                if (loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 3.dp
                        )
                    }
                } else {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        MiniSpecChip(Icons.Outlined.Brush, painting.medium)

                        painting.artStyle?.let {
                            MiniSpecChip(Icons.Outlined.Palette, it)
                        }

                        MiniSpecChip(
                            Icons.Outlined.AspectRatio,
                            "${painting.dimensionWidth.toInt()} x ${painting.dimensionHeight.toInt()} cm"
                        )

                        MiniSpecChip(
                            Icons.Outlined.CalendarToday,
                            painting.yearCreated.toString()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MiniSpecChip(icon: ImageVector, label: String) {
    AssistChip(
        onClick = {},
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = null,
        modifier = Modifier.height(28.dp)
    )
}

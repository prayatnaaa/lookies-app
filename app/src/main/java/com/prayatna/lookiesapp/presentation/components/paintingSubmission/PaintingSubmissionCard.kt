package com.prayatna.lookiesapp.presentation.components.paintingSubmission

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.AspectRatio
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun PaintingSubmissionItem(
    item: EventPainting,
    isLoading: Boolean,
    showDivider: Boolean = true,
    onClick: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {

    val painting = item.painting
    var menuExpanded by remember { mutableStateOf(false) }

    val isAccepted =
        item.status.equals("accepted", ignoreCase = true)

    val isRejected =
        item.status.equals("rejected", ignoreCase = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .animateContentSize()
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {

            AsyncImage(
                model = painting.paintingUrl
                    .replace(
                        "http://172.21.179.110",
                        "http://10.0.2.2"
                    ),
                contentDescription = painting.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(90.dp)
                    .aspectRatio(3f / 4f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant
                    )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    StatusPill(text = item.status)

                    Box {
                        IconButton(
                            enabled = !isLoading,
                            onClick = {
                                menuExpanded = true
                            }
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Outlined.MoreVert,
                                contentDescription = null
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = {
                                menuExpanded = false
                            }
                        ) {

                            if (!isAccepted) {
                                DropdownMenuItem(
                                    text = {
                                        Text("Approve")
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.CheckCircle,
                                            contentDescription = null
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
                                    text = {
                                        Text("Reject")
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Cancel,
                                            contentDescription = null
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
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text =
                        formatRupiah(item.finalPrice),
                    style =
                        MaterialTheme.typography.titleSmall,
                    color =
                        MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    MiniSpecChip(
                        Icons.Outlined.Brush,
                        painting.medium
                    )

                    painting.artStyle?.let {
                        MiniSpecChip(
                            Icons.Outlined.Palette,
                            it
                        )
                    }

                    MiniSpecChip(
                        Icons.Outlined.AspectRatio,
                        "${painting.dimensionWidth.toInt()} × ${
                            painting.dimensionHeight.toInt()
                        } cm"
                    )

                    MiniSpecChip(
                        Icons.Outlined.CalendarToday,
                        painting.yearCreated.toString()
                    )
                }
            }
        }

        Crossfade(isLoading) { loading ->

            if (loading) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        strokeWidth = 3.dp
                    )
                }
            }
        }

        if (showDivider) {
            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline
                    .copy(alpha = 0.12f)
            )
        }
    }
}

@Composable
fun MiniSpecChip(
    icon: ImageVector,
    label: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                MaterialTheme.colorScheme.surfaceContainerLow
            )
            .padding(
                horizontal = 8.dp,
                vertical = 5.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(13.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

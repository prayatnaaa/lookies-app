package com.prayatna.lookiesapp.presentation.components.painting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.utils.formatRupiah

private fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "available" -> Color(0xFF4CAF50)
        "sold" -> Color(0xFFF44336)
        "in_exhibition" -> Color(0xFFFF9800)
        else -> Color.Gray
    }
}

private fun getStatusLabel(status: String): String {
    return when (status.lowercase()) {
        "available" -> "Available"
        "sold" -> "Sold"
        "in_exhibition" -> "Exhibition"
        else -> status
    }
}

@Composable
fun PaintingCard(
    modifier: Modifier = Modifier,
    paintingUrl: String,
    name: String,
    artistName: String? = null,
    price: Double? = null,
    status: String? = null,
    isSelected: Boolean = false,
    showWaterMark: Boolean = false,
    onClick: () -> Unit,
) {
    val highlightColor = MaterialTheme.colorScheme.primary
    val isSold = status.equals("sold", ignoreCase = true)

    ElevatedCard(
        enabled = !isSold,
        shape = RectangleShape,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isSelected) {
                    Modifier.border(3.dp, highlightColor)
                } else {
                    Modifier
                }
            ),
        onClick = onClick
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            // IMAGE
            AsyncImage(
                model = paintingUrl.replace(
                    "http://172.21.179.110",
                    "http://10.0.2.2"
                ),
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // WATERMARK
            if (showWaterMark) {
                WaterMark(modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center))
            }

            // SOLD OVERLAY
            if (isSold) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                )
            }

            // STATUS CHIP
            status?.let {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                    shape = RoundedCornerShape(999.dp),
                    color = Color.Black.copy(alpha = 0.55f)
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (isSold) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = getStatusColor(it),
                                modifier = Modifier.size(14.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        getStatusColor(it),
                                        CircleShape
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = getStatusLabel(it),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // BOTTOM INFO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
                    .padding(8.dp)
            ) {
                Column {

                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    artistName?.let {
                        Text(
                            text = it,
                            color = Color.LightGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    price?.let {
                        Text(
                            text = formatRupiah(it),
                            color = if (!isSold) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                Color.LightGray
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // SELECTED CHECK
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(
                            MaterialTheme.colorScheme.background,
                            CircleShape
                        )
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
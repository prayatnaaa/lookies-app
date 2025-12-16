package com.prayatna.lookiesapp.presentation.components.painting

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.painting.DetailPainting

@Composable
fun DetailPaintingSection(
    painting: DetailPainting,
    showStatus: Boolean = false
) {
    val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {

                AsyncImage(
                    model = painting.paintingUrl.replace("http://172.21.179.110", "http://10.0.2.2"),
                    contentDescription = painting.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (showStatus) {
                    Surface(
                        color = Color(0xFF4CAF50),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 40.dp, end = 16.dp)
                    ) {
                        Text(
                            text = "Available",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                                startY = 300f
                            )
                        )
                )

                Text(
                    text = painting.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {

                InfoRowItem(
                    icon = Icons.Default.Person,
                    label = "Created by",
                    value = painting.artistName ?: "Unknown Artist"
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.4f))

                InfoRowItem(
                    icon = Icons.Default.CalendarToday,
                    label = "Year Created",
                    value = painting.yearCreated.toString()
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.4f))

                InfoRowItem(
                    icon = Icons.Default.AspectRatio,
                    label = "Dimensions",
                    value = "${painting.dimensionHeight} cm x ${painting.dimensionWidth} cm"
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.4f))

                val styleInfo = listOfNotNull(painting.mediumName, painting.artStyleName).joinToString(", ")
                InfoRowItem(
                    icon = Icons.Default.Brush,
                    label = "Medium & Style",
                    value = styleInfo
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "About This Painting",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = painting.description ?: "No description available for this artwork.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
}

@Composable
fun InfoRowItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailPainting() {
    val dummyPainting = DetailPainting(
        id = 1,
        artistId = "art1",
        artistName = "Elena",
        title = "Masters of Abstract: A Decade of Color",
        createdAt = "2023-01-01",
        artStyleId = "style1",
        description = "A retrospective featuring the most innovative abstract painters of the last ten years. Explore diverse mediums and techniques from large-scale canvases.",
        dimensionHeight = 120.0,
        dimensionWidth = 80.0,
        mediumId = "med1",
        paintingUrl = "https://example.com/image.jpg",
        subject = "Abstract",
        updatedAt = null,
        yearCreated = 2023,
        mediumName = "Oil on Canvas",
        artStyleName = "Abstract Expressionism"
    )

    MaterialTheme {
        DetailPaintingSection(
            painting = dummyPainting,
        )
    }
}
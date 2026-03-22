package com.prayatna.lookiesapp.presentation.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.presentation.components.eventlist.EventStatusText
import com.prayatna.lookiesapp.utils.Constants

@Composable
fun HomeEventCard(
    event: Event,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(260.dp)
            .height(200.dp),
        shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box {

            AsyncImage(
                model = event.bannerImageUrl
                    .replace("http://172.21.179.110", "http://10.0.2.2"),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    AnimatedStatusDot(event.status)
                    Spacer(modifier = Modifier.width(6.dp))
                    EventStatusText(
                        status = event.status,
                        isPublishTextToHome = true
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = event.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
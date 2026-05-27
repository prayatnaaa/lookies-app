package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingGallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingGallery.state.EventPaintingGalleryUiEvent
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingGallery.state.EventPaintingGalleryUiState
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventPaintingGalleryScreen(
    uiState: EventPaintingGalleryUiState,
    onEvent: (EventPaintingGalleryUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = { onEvent(EventPaintingGalleryUiEvent.OnBackClicked) },
                title = "Event Gallery"
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading && uiState.paintings.isEmpty() -> {
                    CircularLoading(modifier = Modifier.align(Alignment.Center))
                }
                uiState.errorMessage != null && uiState.paintings.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { onEvent(EventPaintingGalleryUiEvent.LoadPaintings(uiState.eventId)) }) {
                            Text("Retry")
                        }
                    }
                }
                uiState.paintings.isEmpty() -> {
                    Text(
                        text = "No artworks found in this event.",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        itemsIndexed(uiState.paintings) { index, item ->
                            PaintingListItem(
                                item = item,
                                onClick = { onEvent(EventPaintingGalleryUiEvent.OnPaintingClicked(item.id)) }
                            )
                            if (index < uiState.paintings.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    thickness = 0.5.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaintingListItem(
    item: EventPainting,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.painting.paintingUrl.replace("http://172.21.179.110", "http://10.0.2.2"),
            contentDescription = item.painting.title,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.painting.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.participant.artist.fullName ?: "Unknown Artist",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatRupiah(item.finalPrice),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        if (item.status == "sold") {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "SOLD",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ZoomInMap
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.presentation.chat.privateChat.navigateToPrivateChat
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.eventPainting.EventOriginCard
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.painting.WaterMark
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state.EventPaintingDetailEvent
import com.prayatna.lookiesapp.utils.NavigationRoutes
import com.prayatna.lookiesapp.utils.formatDate
import com.prayatna.lookiesapp.utils.formatRupiah
import java.time.OffsetDateTime

@Composable
fun EventPaintingDetailScreen(
    navController: NavController,
    id: String,
    viewModel: EventPaintingDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getEventPaintingDetail(id)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    var showFullImage by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is EventPaintingDetailEvent.NavigateToChat -> {
                    navController.navigateToPrivateChat(
                        partyName = event.otherPartyName,
                        conversationId = event.conversationId,
                        merchantId = event.merchantId,
                        metadataType = "painting",
                        metadataId = id,
                        metadataImageUrl = state.data?.painting?.paintingUrl,
                        metadataTitle = state.data?.painting?.title
                    )
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            state.data?.let { data ->
                FloatingActionButton(
                    onClick = {
                        viewModel.onChatArtistClicked(
                            artistId = data.artistId,
                            artistName = data.participant.artist.fullName ?: data.participant.artist.username ?: "Artist"
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Chat,
                        contentDescription = "Chat Artist"
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BackTopBar(
                onBackClick = {
                    navController.popBackStack()
                },
                title = "Artwork Detail",
            )
        },
        bottomBar = {
            state.data?.let { data ->
                val event = data.participant.event
                PaintingPurchaseBottomBar(
                    price = data.finalPrice,
                    isSold = data.status == "sold",
                    startDate = event.startDate,
                    endDate = event.endDate,
                    onBuyClick = {
                        Log.d("CHECK-PAINTING", data.id)
                        navController.navigate("${NavigationRoutes.CHECKOUT}/painting/${data.id}/1")
                    }
                )
            }
        }
    ) { innerPadding ->

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularLoading()
            }
        }
        else if (state.data != null) {
            val item = state.data!!
            val painting = item.painting
            val participant = item.participant
            val artist = participant.artist
            val event = participant.event

            if (showFullImage) {
                Dialog(
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false
                    ),
                    onDismissRequest = {
                        showFullImage = false
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                    ) {

                        AsyncImage(
                            model = painting.paintingUrl.replace(
                                "http://172.21.179.110",
                                "http://10.0.2.2"
                            ),
                            contentDescription = painting.title,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                        WaterMark(modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center))

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(20.dp)
                                .clickable {
                                    showFullImage = false
                                }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    AsyncImage(
                        model = painting.paintingUrl.replace("http://172.21.179.110", "http://10.0.2.2"),
                        contentDescription = painting.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 300.dp, max = 550.dp)
                    )

                    Icon(
                        imageVector = Icons.Default.ZoomInMap,
                        contentDescription = "Zoom",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .background(
                                Color.Black.copy(alpha = 0.5f),
                                CircleShape
                            )
                            .clickable {
                                showFullImage = true
                            }
                            .padding(8.dp)
                    )

                    WaterMark(modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center))
                }

                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = painting.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Created in ${painting.yearCreated}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onChatArtistClicked(
                                    artistId = item.artistId,
                                    artistName = artist.fullName ?: artist.username ?: "Artist"
                                )
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = artist.profileUrl?.replace("http://172.21.179.110", "http://10.0.2.2")
                                ?: "https://ui-avatars.com/api/?name=${artist.username}",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = artist.fullName ?: artist.username ?: "Unknown Artist",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Artist",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    EventOriginCard(
                        eventTitle = event.title,
                        organizerName = event.organizer.legalName,
                        endDate = event.endDate,
                        location = event.location,
                        eventFormat = event.eventFormat.name,
                        onClick = {
                            navController.navigate("${NavigationRoutes.DETAIL_EVENT}/${event.id}")
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Artwork Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SpecItem(
                            icon = Icons.Outlined.Brush,
                            label = "Medium",
                            value = painting.medium,
                            modifier = Modifier.weight(1f)
                        )
                        SpecItem(
                            icon = Icons.Outlined.Straighten,
                            label = "Dimensions",
                            value = "${painting.dimensionHeight} x ${painting.dimensionWidth} cm",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        painting.artStyle?.let { style ->
                            SuggestionChip(
                                onClick = {},
                                label = { Text(style) },
                                icon = {
                                    Icon(
                                        Icons.Default.Palette,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }
                        painting.subject?.let { subject ->
                            SuggestionChip(
                                onClick = {},
                                label = { Text(subject) },
                                icon = {
                                    Icon(
                                        Icons.AutoMirrored.Filled.Label,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = painting.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun SpecItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun PaintingPurchaseBottomBar(
    price: Double,
    isSold: Boolean,
    startDate: String,
    endDate: String,
    onBuyClick: () -> Unit
) {
    val now = remember { OffsetDateTime.now() }
    val start = remember(startDate) {
        try { OffsetDateTime.parse(startDate) } catch (_: Exception) { null }
    }
    val end = remember(endDate) {
        try { OffsetDateTime.parse(endDate) } catch (_: Exception) { null }
    }

    val isStarted: Boolean = start?.let { now.isAfter(it) || now.isEqual(it) } ?: true
    val isFinished: Boolean = end?.let { now.isAfter(it) } ?: false
    val isBuyable: Boolean = isStarted && !isFinished && !isSold

    val statusLabel = when {
        !isStarted -> "Start on"
        !isFinished -> "Close on"
        else -> "Ended on"
    }

    val statusDate = when {
        !isStarted -> formatDate(startDate)
        else -> formatDate(endDate)
    }

    Surface(
        shadowElevation = 16.dp,
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = statusDate,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (!isBuyable && !isSold) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = formatRupiah(price),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            val buttonText = when {
                isSold -> "Sold Out"
                !isStarted -> "Buy Now"
                isFinished -> "Sale Ended"
                else -> "Buy Now"
            }

            Button(
                onClick = onBuyClick,
                enabled = isBuyable,
                modifier = Modifier
                    .height(48.dp)
                    .width(150.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isBuyable) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = buttonText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

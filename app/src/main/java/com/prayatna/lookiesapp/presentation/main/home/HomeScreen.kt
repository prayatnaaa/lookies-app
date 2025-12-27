package com.prayatna.lookiesapp.presentation.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.presentation.components.SearchBar
import com.prayatna.lookiesapp.presentation.components.home.CategoryChip
import com.prayatna.lookiesapp.presentation.components.home.FeaturedEventBanner
import com.prayatna.lookiesapp.presentation.components.home.HomeEventCard
import com.prayatna.lookiesapp.presentation.components.home.HomePaintingCard
import com.prayatna.lookiesapp.presentation.components.home.SectionHeader
import java.util.UUID

@Composable
fun HomeScreen(
    navController: NavController
) {
    val categories = remember { listOf("All", "Abstract", "Realism", "Impressionism", "Surrealism", "Portrait") }
    val dummyEvents = remember { generateDummyEvents() }
    val dummyPaintings = remember { generateDummyPaintings() }
    val featuredEvent = dummyEvents.first()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            HomeTopBar(
                userName = "Prayatna",
                onCartClick = { /* Navigate Cart */ },
                onNotifClick = { /* Navigate Notif */ }
            )
        }
    ) { innerPadding ->

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = 80.dp,
                start = 16.dp,
                end = 16.dp
            ),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalItemSpacing = 12.dp
        ) {

            item(span = StaggeredGridItemSpan.FullLine) {
                Column {
                    SearchBar(onClick = { /* Navigate Search */ })
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Featured Event",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    FeaturedEventBanner(event = featuredEvent)
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            item(span = StaggeredGridItemSpan.FullLine) {
                // LazyRow harus di dalam item agar bisa scroll horizontal
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(label = category, isSelected = category == "All")
                    }
                }
            }

            item(span = StaggeredGridItemSpan.FullLine) {
                Column {
                    SectionHeader(title = "Ongoing Events", onSeeAll = {})
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(dummyEvents) { event ->
                            HomeEventCard(event = event)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            item(span = StaggeredGridItemSpan.FullLine) {
                SectionHeader(title = "Curated For You", onSeeAll = {})
            }

            items(dummyPaintings) { painting ->
                HomePaintingCard(painting = painting)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    userName: String,
    onCartClick: () -> Unit,
    onNotifClick: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Hello,",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        actions = {
            IconButton(onClick = onNotifClick) {
                Icon(Icons.Default.Notifications, contentDescription = "Notification")
            }
            IconButton(onClick = onCartClick) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

fun generateDummyEvents(): List<Event> {
    return listOf(
        Event(
            id = "1",
            title = "Jogja Art Weeks 2025",
            bannerImageUrl = "https://images.unsplash.com/photo-1577083165633-14ebcdb0f658?q=80&w=600&auto=format&fit=crop",
            startDate = "12 Oct",
            endDate = "20 Nov",
            location = "Yogyakarta",
            eventType = "Exhibition",
            eventFormat = "Offline",
            status = "Ongoing",
            createdAt = "2025-01-01",
            about = "Biggest art event.",
            locationUrl = "",
            organizerId = "1"
        ),
        Event(
            id = "2",
            title = "Bali Creative Fest",
            bannerImageUrl = "https://images.unsplash.com/photo-1561214115-f2f134cc4912?q=80&w=600&auto=format&fit=crop",
            startDate = "01 Dec",
            endDate = "05 Dec",
            location = "Denpasar, Bali",
            eventType = "Festival",
            eventFormat = "Offline",
            status = "Upcoming",
            createdAt = "2025-01-01",
            about = "Creative arts festival.",
            locationUrl = "",
            organizerId = "1"
        ),
        Event(
            id = "3",
            title = "Jakarta Biennale",
            bannerImageUrl = "https://images.unsplash.com/photo-1536924940846-227afb31e2a5?q=80&w=600&auto=format&fit=crop",
            startDate = "10 Jan",
            endDate = "10 Feb",
            location = "Jakarta Pusat",
            eventType = "Biennale",
            eventFormat = "Offline",
            status = "Upcoming",
            createdAt = "2025-01-01",
            about = "Contemporary art.",
            locationUrl = "",
            organizerId = "1"
        )
    )
}

fun generateDummyPaintings(): List<Painting> {
    return List(10) { index ->
        val width = if (index % 2 == 0) 300 else 400
        val height = if (index % 2 == 0) 400 else 300

        Painting(
            id = index,
            title = "The Abstract Mind #$index",
            description = "A wonderful piece of art.",
            paintingUrl = "https://images.unsplash.com/photo-1579783902614-a3fb39279c15?q=80&w=$width&h=$height&auto=format&fit=crop",
            price = (1000000..5000000).random().toDouble(),
            yearCreated = 2024,
            subject = "Abstract",
            dimensionHeight = height.toDouble(),
            dimensionWidth = width.toDouble(),
            artistId = "Artist ${index + 1}",
            artStyle = UUID.randomUUID().toString(),
            medium = UUID.randomUUID().toString()
        )
    }
}
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
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.SearchBar
import com.prayatna.lookiesapp.presentation.components.home.FeaturedEventBanner
import com.prayatna.lookiesapp.presentation.components.home.HomeEventCard
import com.prayatna.lookiesapp.presentation.components.home.HomePaintingCard
import com.prayatna.lookiesapp.presentation.components.home.SectionHeader
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val featuredEvent = state.events.firstOrNull()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (state.user != null) {
                val user = state.user
                user?.let { data ->
                    HomeTopBar(
                        userName = data.username!!,
                    )
                }
            }
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
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    SearchBar(onClick = {
                        navController.navigate(NavigationRoutes.SEARCH)
                    })
                    Spacer(modifier = Modifier.height(24.dp))

                    if (featuredEvent != null) {
                        Text(
                            text = "Featured Event",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        FeaturedEventBanner(
                            event = featuredEvent,
                            onClick = {
                                navController.navigate("${NavigationRoutes.DETAIL_EVENT}/${featuredEvent.id}")
                            })
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }

            item(span = StaggeredGridItemSpan.FullLine) {
                Column {
                    SectionHeader(title = "Events", onSeeAll = {
                        navController.navigate(NavigationRoutes.EVENT_LIST)
                    })
                    if (state.isLoadingEvents) {
                        androidx.compose.foundation.layout.Box(
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            androidx.compose.material3.CircularProgressIndicator()
                        }
                    } else if (state.events.isEmpty()) {
                        com.prayatna.lookiesapp.presentation.components.EmptyState(
                            modifier = Modifier.height(200.dp),
                            title = "No Events",
                            description = "No upcoming events available at the moment.",
                            icon = androidx.compose.material.icons.Icons.Default.Event
                        )
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(state.events) { event ->
                                HomeEventCard(event = event, onClick = {
                                    navController.navigate("${NavigationRoutes.DETAIL_EVENT}/${event.id}")
                                })
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            item(span = StaggeredGridItemSpan.FullLine) {
                SectionHeader(title = "Curated For You", onSeeAll = {})
            }

            if (state.isLoadingPaintings) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier.fillMaxWidth().height(150.dp),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        androidx.compose.material3.CircularProgressIndicator()
                    }
                }
            } else if (state.eventPaintings.isEmpty()) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    com.prayatna.lookiesapp.presentation.components.EmptyState(
                        modifier = Modifier.height(200.dp),
                        title = "No Paintings",
                        description = "No curated paintings available at the moment.",
                        icon = androidx.compose.material.icons.Icons.Default.Palette
                    )
                }
            } else {
                items(state.eventPaintings) { painting ->
                    HomePaintingCard(data = painting, onClick = {
                        navController.navigate("${NavigationRoutes.DETAIL_EVENT_PAINTING}/${painting.id}")
                    })
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    userName: String,
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
//        actions = {
//            IconButton(onClick = onNotifClick) {
//                Icon(Icons.Default.Notifications, contentDescription = "Notification")
//            }
//            IconButton(onClick = onCartClick) {
//                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
//            }
//        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}
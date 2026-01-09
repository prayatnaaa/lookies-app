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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.SearchBar
import com.prayatna.lookiesapp.presentation.components.home.CategoryChip
import com.prayatna.lookiesapp.presentation.components.home.FeaturedEventBanner
import com.prayatna.lookiesapp.presentation.components.home.HomeEventCard
import com.prayatna.lookiesapp.presentation.components.home.HomePaintingCard
import com.prayatna.lookiesapp.presentation.components.home.SectionHeader
import com.prayatna.lookiesapp.presentation.loading.MainLoadingScreen
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val categories = remember {
        listOf("All", "Abstract", "Realism", "Impressionism", "Surrealism", "Portrait")
    }

    val featuredEvent = state.events.firstOrNull()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            if (state.user != null) {
                val user = state.user
                user?.let { data ->
                    HomeTopBar(
                        userName = data.username!!,
                        onCartClick = { },
                        onNotifClick = { }
                    )
                }
            }
        }
    ) { innerPadding ->

        if (state.isLoadingEvents || state.isLoadingPaintings) {
            MainLoadingScreen()
        } else {
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
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        items(categories) { category ->
                            CategoryChip(
                                label = category,
                                isSelected = category == "All"
                            )
                        }
                    }
                }

                if (state.events.isNotEmpty()) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Column {
                            SectionHeader(title = "Events", onSeeAll = {})
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
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }

                item(span = StaggeredGridItemSpan.FullLine) {
                    SectionHeader(title = "Curated For You", onSeeAll = {})
                }

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
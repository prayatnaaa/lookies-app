package com.prayatna.lookiesapp.presentation.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.chat.conversationList.navigateToConversationList
import com.prayatna.lookiesapp.presentation.notification.navigateToNotifications
import com.prayatna.lookiesapp.presentation.components.EmptyState
import com.prayatna.lookiesapp.presentation.components.SearchBar
import com.prayatna.lookiesapp.presentation.components.home.FeaturedEventBanner
import com.prayatna.lookiesapp.presentation.components.home.HomeEventCard
import com.prayatna.lookiesapp.presentation.components.home.HomePaintingCard
import com.prayatna.lookiesapp.presentation.components.home.SectionHeader
import com.prayatna.lookiesapp.utils.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val featuredEvent = state.events.firstOrNull()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = {
            viewModel.refreshHome()
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                if (state.user != null) {
                    val user = state.user
                    user?.let { data ->
                    HomeTopBar(
                            onNotifClick = {
                                navController.navigateToNotifications()
                            },
                            onChatClick = {
                                navController.navigateToConversationList()
                            },
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
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else if (state.events.isEmpty()) {
                            EmptyState(
                                modifier = Modifier.height(200.dp),
                                title = "No Events",
                                description = "No upcoming events available at the moment.",
                                icon = Icons.Default.Event
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
                    SectionHeader(title = "Curated For You", onSeeAll = {
                        navController.navigate(NavigationRoutes.EVENT_PAINTING_LIST)
                    })
                }

                if (state.isLoadingPaintings) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    val visiblePaintings = state.eventPaintings.filter {
                        it.participant.event.status in listOf("published", "upcoming", "ongoing")
                    }

                    if (visiblePaintings.isEmpty()) {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            EmptyState(
                                modifier = Modifier.height(200.dp),
                                title = "No Paintings",
                                description = "No curated paintings available at the moment.",
                                icon = Icons.Default.Palette
                            )
                        }
                    } else {
                        items(visiblePaintings) { painting ->
                            HomePaintingCard(data = painting, onClick = {
                                navController.navigate("${NavigationRoutes.DETAIL_EVENT_PAINTING}/${painting.id}")
                            })
                        }
                    }
                }
            }
        }
    }
    
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    userName: String,
    onNotifClick: () -> Unit = {},
    onChatClick: () -> Unit = {}
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
            IconButton(onClick = onChatClick) {
                Icon(Icons.AutoMirrored.Filled.Message, contentDescription = "Cart")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}
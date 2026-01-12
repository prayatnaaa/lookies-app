package com.prayatna.lookiesapp.presentation.artistDashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.artistDashboard.state.ArtistDashboardUiState
import com.prayatna.lookiesapp.presentation.components.artistDashboard.DashboardActionItem
import com.prayatna.lookiesapp.presentation.components.artistDashboard.DashboardStatCard
import com.prayatna.lookiesapp.presentation.components.artistDashboard.EarningsOverviewCard
import com.prayatna.lookiesapp.presentation.components.artistDashboard.RevenueBarChart
import com.prayatna.lookiesapp.utils.NavigationRoutes
import com.prayatna.lookiesapp.utils.formatRupiah

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDashboardScreen(
    navController: NavController,
    viewModel: ArtistDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Artist Studio",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Manage your art business",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                     navController.navigate(NavigationRoutes.UPLOAD_PAINTING)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Upload Art") }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (val currentState = state) {
                is ArtistDashboardUiState.Loading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading studio...", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                is ArtistDashboardUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.WarningAmber,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Failed to load dashboard",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                is ArtistDashboardUiState.Success -> {
                    val summary = currentState.data.summary
                    val sales = currentState.data.monthlySales

                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            EarningsOverviewCard(
                                totalEarnings = formatRupiah(summary.totalEarnings),
                                pendingPayout = formatRupiah(summary.pendingPayout)
                            )
                        }

                        item {
                            Text(
                                "Performance",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                DashboardStatCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Items Sold",
                                    value = summary.paintingsSold.toString(),
                                    icon = Icons.Outlined.MonetizationOn,
                                    iconTint = Color(0xFF4CAF50),
                                    containerColor = Color(0xFFE8F5E9)
                                )
                                DashboardStatCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Rating",
                                    value = String.format("%.1f", summary.averageRating),
                                    icon = Icons.Default.Star,
                                    iconTint = Color(0xFFFFB300), // Amber
                                    containerColor = Color(0xFFFFF8E1) // Light Amber
                                )
                                DashboardStatCard(
                                    modifier = Modifier.weight(1f),
                                    title = "On Sale",
                                    value = summary.paintingsOnSale.toString(),
                                    icon = Icons.Outlined.Inventory2,
                                    iconTint = MaterialTheme.colorScheme.primary,
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                )
                            }
                        }

                        item {
                            Text(
                                "Revenue Trend",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            RevenueBarChart(data = sales)
                        }

                        item {
                            Text(
                                "Management",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                DashboardActionItem(
                                    title = "My Artworks",
                                    subtitle = "Manage portfolio, pricing & stock",
                                    icon = Icons.Filled.Brush,
                                    onClick = {
                                        navController.navigate("${NavigationRoutes.PERSONAL_PAINTING}/${currentState.data.summary.artistId}")
                                    }
                                )
                                DashboardActionItem(
                                    title = "Exhibition History",
                                    subtitle = "Track event participation status",
                                    icon = Icons.Filled.Event,
                                    onClick = {
                                    }
                                )
                            }
                        }

                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.prayatna.lookiesapp.domain.model.transaction.MerchantBalanceLog
import com.prayatna.lookiesapp.presentation.components.artistDashboard.DashboardActionItem
import com.prayatna.lookiesapp.presentation.components.artistDashboard.DashboardStatCard
import com.prayatna.lookiesapp.presentation.components.artistDashboard.EarningsOverviewCard
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
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    state.summary?.let { summary ->
                        navController.navigate("${NavigationRoutes.UPLOAD_PAINTING}/${summary.businessId}")
                    }
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
            if (state.isLoading && state.summary == null) {
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
            } else if (state.errorMessage != null && state.summary == null) {
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
                        text = state.errorMessage!!,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                state.summary?.let { summary ->
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            EarningsOverviewCard(
                                onClick = {
                                    navController.navigate(
                                        "${NavigationRoutes.MONTHLY_FINANCE_LIST_SCREEN}/${summary.businessId}"
                                    )
                                },
                                totalEarnings = formatRupiah(summary.totalRevenue),
                                pendingPayout = formatRupiah(state.pendingOrderSplits?.totalAmount?.toDouble() ?: 0.0)
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
                                    value = summary.totalSoldPaintings.toString(),
                                    icon = Icons.Outlined.MonetizationOn,
                                    iconTint = Color(0xFF4CAF50),
                                    containerColor = Color(0xFFE8F5E9)
                                )
                                DashboardStatCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Rating",
                                    value = String.format("%.1f", summary.avgRating),
                                    icon = Icons.Default.Star,
                                    iconTint = Color(0xFFFFB300),
                                    containerColor = Color(0xFFFFF8E1)
                                )
                                DashboardStatCard(
                                    modifier = Modifier.weight(1f),
                                    title = "On Sale",
                                    textColor = MaterialTheme.colorScheme.primary,
                                    value = summary.totalPaintings.toString(),
                                    icon = Icons.Outlined.Inventory2,
                                    iconTint = MaterialTheme.colorScheme.primary,
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                )
                            }
                        }

                        item {
                            Text(
                                "Overview",
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
                                    title = "This Month",
                                    value = formatRupiah(summary.currentMonthRevenue),
                                    icon = Icons.AutoMirrored.Outlined.TrendingUp,
                                    iconTint = Color(0xFF2196F3),
                                    containerColor = Color(0xFFE3F2FD)
                                )
                                DashboardStatCard(
                                    modifier = Modifier.weight(1f),
                                    title = "To Ship",
                                    value = summary.ordersToShip.toString(),
                                    icon = Icons.Default.LocalShipping,
                                    iconTint = Color(0xFFFF7043),
                                    containerColor = Color(0xFFFBE9E7)
                                )
                            }
                        }

                        if (state.balanceLogs.isNotEmpty()) {
                            item {
                                Text(
                                    "Balance History",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            items(state.balanceLogs.take(5)) { log ->
                                BalanceLogCard(log)
                            }
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
                                        navController.navigate("${NavigationRoutes.PERSONAL_PAINTING}/${summary.businessId}")
                                    }
                                )
                                DashboardActionItem(
                                    title = "Exhibition History",
                                    subtitle = "Track event participation status",
                                    icon = Icons.Filled.Event,
                                    onClick = {
                                        navController.navigate(
                                            "${NavigationRoutes.EXHIBITION_HISTORY}/${summary.businessId}"
                                        )
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

@Composable
private fun BalanceLogCard(
    log: MerchantBalanceLog
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = log.transactionType.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (log.amount > 0) MaterialTheme.colorScheme.primary 
                            else MaterialTheme.colorScheme.error
                )
                Text(
                    text = log.createdAt.take(10),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Amount : ${formatRupiah(log.amount.toDouble())}",
                fontWeight = FontWeight.Bold
            )
            
            log.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Balance After : ${formatRupiah(log.balanceAfter.toDouble())}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

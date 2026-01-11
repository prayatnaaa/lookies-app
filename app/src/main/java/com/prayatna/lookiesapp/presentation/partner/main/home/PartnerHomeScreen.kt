package com.prayatna.lookiesapp.presentation.partner.main.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.loading.SkeletonBox
import com.prayatna.lookiesapp.presentation.components.partner.DashboardActionItem
import com.prayatna.lookiesapp.presentation.components.partner.FinancialOverviewCard
import com.prayatna.lookiesapp.presentation.components.partner.StatCard
import com.prayatna.lookiesapp.presentation.partner.main.home.state.PartnerHomeUiState
import com.prayatna.lookiesapp.utils.NavigationRoutes
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerHomeScreen(
    navController: NavController,
    viewModel: PartnerHomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        if (state is PartnerHomeUiState.Success) {
                            Text(
                                text = "Hello, ${(state as PartnerHomeUiState.Success).data.partnerName}!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            SkeletonBox(
                                modifier = Modifier
                                    .width(180.dp)
                                    .height(22.dp)
                            )
                        }

                        Text(
                            text = "Here's your update today",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notification")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavigationRoutes.CREATE_EVENT) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create Event")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
            when (val currentState = state) {
                is PartnerHomeUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is PartnerHomeUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Failed to load data", color = MaterialTheme.colorScheme.error)
                        Text(text = currentState.message, style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.getDashboardData() }) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Text("Retry")
                        }
                    }
                }

                is PartnerHomeUiState.Success -> {
                    val data = currentState.data

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        item {
                            FinancialOverviewCard(
                                totalRevenue = formatRupiah(data.totalRevenue),
                                pendingPayout = formatRupiah(data.pendingPayout),
                                salesGrowth = "View Reports >"
                            )
                        }

                        item {
                            Text(
                                text = "Performance",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                StatCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Active Events",
                                    value = data.activeEvents.toString(),
                                    icon = Icons.Outlined.EventAvailable
                                )
                                StatCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Tickets Sold",
                                    value = data.totalTicketsSold.toString(),
                                    icon = Icons.Outlined.ConfirmationNumber
                                )
                                StatCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Total Events",
                                    value = data.totalEventsCreated.toString(),
                                    icon = Icons.Default.Event
                                )
                            }
                        }

                        item {
                            Text(
                                text = "Management",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                DashboardActionItem(
                                    title = "My Events",
                                    subtitle = "Manage your exhibitions and shows",
                                    icon = Icons.Filled.Event,
                                    onClick = { navController.navigate(NavigationRoutes.SELF_EVENT_LIST) }
                                )

                                DashboardActionItem(
                                    title = "Collaborators",
                                    subtitle = "Manage artists and partners",
                                    icon = Icons.Filled.Group,
                                    onClick = {
                                        navController.navigate(NavigationRoutes.PARTICIPANT_LIST)
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
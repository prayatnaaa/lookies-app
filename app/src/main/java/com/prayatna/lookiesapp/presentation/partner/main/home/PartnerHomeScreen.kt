package com.prayatna.lookiesapp.presentation.partner.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.partner.DashboardActionItem
import com.prayatna.lookiesapp.presentation.components.partner.FinancialOverviewCard
import com.prayatna.lookiesapp.presentation.components.partner.StatCard
import com.prayatna.lookiesapp.utils.NavigationRoutes

data class DashboardStat(
    val label: String,
    val value: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerHomeScreen(
    navController: NavController
) {
    // Dummy Data
    val stats = listOf(
        DashboardStat("Active Events", "12", Icons.Filled.Event),
        DashboardStat("Offers", "5", Icons.Outlined.LocalOffer),
        DashboardStat("Tickets Sold", "128", Icons.Outlined.ConfirmationNumber),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Hello, Partner!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Here's your update today",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Navigate to Notif */ }) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notification")
                    }
                }
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
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            item {
                FinancialOverviewCard(
                    totalRevenue = "Rp 15.400.000",
                    salesGrowth = "+12% vs last month"
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
                    stats.forEach { stat ->
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = stat.label,
                            value = stat.value,
                            icon = stat.icon
                        )
                    }
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
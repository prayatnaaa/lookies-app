package com.prayatna.lookiesapp.presentation.partner.main.home

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.transaction.MerchantBalanceLog
import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReport
import com.prayatna.lookiesapp.domain.model.transaction.PendingOrderSplits
import com.prayatna.lookiesapp.presentation.partner.main.home.state.PartnerHomeEvent
import com.prayatna.lookiesapp.presentation.partner.main.home.state.PartnerHomeUiState
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerHomeScreen(
    state: PartnerHomeUiState,
    onEvent: (PartnerHomeEvent) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val isApproved = state.profile?.payoutEnabled == true
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    DashboardHeader(
                        onChatClick = {
                            onEvent(PartnerHomeEvent.ChatClick)
                        },
                        onMemberClick = {
                            onEvent(PartnerHomeEvent.MemberListClicked)
                        },
                        onEditProfileClick = {
                            onEvent(PartnerHomeEvent.EditProfileClicked)
                        },
                        title = state.profile?.tradingName
                            ?: state.profile?.legalName
                            ?: "Loading..."
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onEvent(PartnerHomeEvent.BackClicked)
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (isApproved) {
                        onEvent(PartnerHomeEvent.CreateEventClicked)
                    }
                },
                expanded = true,
                containerColor = if (isApproved)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant,
                icon = {
                    Icon(Icons.Default.Add, null)
                },
                text = {
                    Text("Create Event")
                }
            )
        }
    ) { padding ->

        if (state.isLoading && state.profile == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            if (!isApproved) {
                item {
                    ApprovalPendingCard()
                }
            }

            item {
                RevenueOverviewCard(
                    balanceLogs = state.balanceLogs,
                    pendingOrderSplits = state.pendingOrderSplits,
                    isLoading = state.isLoading,
                    enabled = isApproved,
                onClick = {
                        onEvent(PartnerHomeEvent.MonthlyFinanceClicked)
                    }
                )
            }

            item {
                QuickStatsSection(state)
            }

            item {
                ActionGrid(onEvent, enabled = isApproved)
            }

            if (state.monthlyFinancialReport.isNotEmpty()) {
                item {
                    Text(
                        "Monthly Finance",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(state.monthlyFinancialReport) { report ->
                    MonthlyReportCard(report)
                }
            }

            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }
}

@Composable
private fun DashboardHeader(
    title: String,
    onMemberClick: () -> Unit,
    onChatClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Row {
            IconButton(
                onClick = onEditProfileClick
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Edit"
                )
            }
            IconButton(
                onClick = onMemberClick
            ) {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "Member"
                )
            }
            IconButton(
                onClick = onChatClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Message,
                    contentDescription = "Chat"
                )
            }
        }
    }
}

/* --------------------------------------------------- */

@Composable
private fun RevenueOverviewCard(
    balanceLogs: List<MerchantBalanceLog>,
    pendingOrderSplits: PendingOrderSplits?,
    isLoading: Boolean,
    onClick: () -> Unit,
    enabled: Boolean
) {
    val latestBalance = balanceLogs.firstOrNull()?.balanceAfter
    val pending = pendingOrderSplits?.totalAmount

    ElevatedCard(
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        onClick = {
            if (enabled) {
                onClick()
            }
        },
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Current Balance",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "View Finance Details",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Balance Amount
            if (isLoading && latestBalance == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f))
                )
            } else {
                Text(
                    text = formatRupiah(latestBalance?.toDouble() ?: 0.0),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Pending Payout Section
            if ((pending ?: 0L) > 0) {
                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = "Pending Payout",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }

                        Text(
                            text = formatRupiah(pending?.toDouble() ?: 0.0),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
/* --------------------------------------------------- */

@Composable
private fun QuickStatsSection(
    state: PartnerHomeUiState
) {
    val dashboard = state.dashboard

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        SmallStatCard(
            title = "Events",
            value = dashboard?.activeEvents?.toString(),
            isLoading = state.isLoading && dashboard == null,
            modifier = Modifier.weight(1f)
        )

        SmallStatCard(
            title = "Tickets",
            value = dashboard?.totalTicketsSold?.toString(),
            isLoading = state.isLoading && dashboard == null,
            modifier = Modifier.weight(1f)
        )

        SmallStatCard(
            title = "Payout",
            value = when {
                state.profile == null -> null
                state.profile.payoutEnabled -> "ON"
                else -> "OFF"
            },
            isLoading = state.isLoading &&
                    state.profile == null,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SmallStatCard(
    title: String,
    value: String?,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (isLoading) {
                Text(
                    text = "...",
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = value ?: "-",
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                title,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

/* --------------------------------------------------- */

@Composable
private fun ActionGrid(
    onEvent: (PartnerHomeEvent) -> Unit,
    enabled: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            "Management",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCircle(
                enabled = enabled,
                title = "Events",
                icon = {
                    Icon(Icons.Default.Event, null)
                },
                onClick = {
                    onEvent(
                        PartnerHomeEvent.MyEventsClicked
                    )
                },
                modifier = Modifier.weight(1f)
            )

//            ActionCircle(
//                title = "Refund",
//                icon = {
//                    Icon(Icons.Default.Payments, null)
//                },
//                onClick = {
//                    onEvent(
//                        PartnerHomeEvent.RefundClicked
//                    )
//                },
//                modifier = Modifier.weight(1f),
//                enabled = enabled
//            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCircle(
                title = "Painting",
                icon = {
                    Icon(Icons.Default.Group, null)
                },
                onClick = {
                    onEvent(
                        PartnerHomeEvent.PaintingClicked
                    )
                },
                modifier = Modifier.weight(1f),
                enabled = enabled
            )

            ActionCircle(
                title = "Shipment",
                icon = {
                    Icon(Icons.Default.LocalShipping, null)
                },
                onClick = {
                    onEvent(
                        PartnerHomeEvent.ShipmentClicked
                    )
                },
                modifier = Modifier.weight(1f),
                enabled = enabled
            )
        }
    }
}

@Composable
private fun ActionCircle(
    title: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
) {
    ElevatedCard(
        enabled = enabled,
        onClick = {
            if (enabled) onClick()
        },
        modifier = modifier,
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Surface(
                shape = CircleShape,
                tonalElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier
                        .padding(14.dp)
                        .alpha(if (enabled) 1f else 0.45f)
                ) {
                    icon()
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                modifier = Modifier.alpha(
                    if (enabled) 1f else 0.5f
                )
            )
        }
    }
}

/* --------------------------------------------------- */

@Composable
private fun MonthlyReportCard(
    report: MonthlyFinancialReport
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                report.reportMonth,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Orders : ${report.totalOrders}")
            Text("Gross : ${formatRupiah(report.grossRevenue)}")
            Text("Fee : ${formatRupiah(report.platformFees)}")
            Text("Gateway : ${formatRupiah(report.paymentGatewayFees)}")

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                "Net : ${formatRupiah(report.netRevenue)}",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ApprovalPendingCard() {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Approval Required",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Your partner account is still under review. Features will be available maximum 5 minutes after approval from admin.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

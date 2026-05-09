package com.prayatna.lookiesapp.presentation.partner.main.home

/*
UPDATED SCREEN IDEA
- Modern dashboard style
- Circular revenue overview
- Monthly finance cards
- Cleaner hierarchy
- Existing MVI compatible

Requires:
PartnerHomeUiState now has:
monthlyFinancialReport: List<MonthlyFinancialReport> = emptyList()
*/

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReport
import com.prayatna.lookiesapp.presentation.partner.main.home.state.PartnerHomeEvent
import com.prayatna.lookiesapp.presentation.partner.main.home.state.PartnerHomeUiState
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerHomeScreen(
    state: PartnerHomeUiState,
    onEvent: (PartnerHomeEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    DashboardHeader(
                        onMemberClick = {
                            onEvent(PartnerHomeEvent.MemberListClicked)
                        },
                        title = state.profile?.tradingName
                            ?: state.profile?.legalName
                            ?: "Dashboard"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(PartnerHomeEvent.BackClicked) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onEvent(PartnerHomeEvent.CreateEventClicked)
                },
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Create Event") }
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

            item {
                RevenueOverviewCard(
                    reports = state.monthlyFinancialReport,
                    onClick = {
                        onEvent(PartnerHomeEvent.MonthlyFinanceClicked)
                    }
                )
            }

            item {
                QuickStatsSection(state)
            }

            item {
                ActionGrid(onEvent)
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

/* --------------------------------------------------- */

@Composable
private fun DashboardHeader(
    title: String,
    onMemberClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
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

        IconButton(
            onClick = onMemberClick
        ) {
            Icon(
                imageVector = Icons.Default.People,
                contentDescription = "Member"
            )
        }
    }
}

/* --------------------------------------------------- */

@Composable
private fun RevenueOverviewCard(
    reports: List<MonthlyFinancialReport>,
    onClick: () -> Unit
) {
    val latest = reports.firstOrNull()
    val gross = latest?.grossRevenue ?: 0.0
    val net = latest?.netRevenue ?: 0.0

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            CircularRevenueChart(
                gross = gross,
                net = net
            )

            Spacer(modifier = Modifier.width(18.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Revenue Overview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Gross : Rp ${gross.toLong()}")
                Text("Net : Rp ${net.toLong()}")

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    "Latest month",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun CircularRevenueChart(
    gross: Double,
    net: Double
) {
    val progress =
        if (gross <= 0.0) 0f
        else (net / gross).toFloat().coerceIn(0f, 1f)

    // 1. Resolve colors here in the @Composable scope!
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val progressColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .size(110.dp)
            .clickable {

            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            val stroke = 12.dp.toPx()
            val canvasSize = min(size.width, size.height)

            drawArc(
                color = trackColor, // 2. Use the resolved color
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                ),
                size = Size(canvasSize, canvasSize),
                topLeft = Offset.Zero
            )

            drawArc(
                color = progressColor, // 2. Use the resolved color
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                ),
                size = Size(canvasSize, canvasSize),
                topLeft = Offset.Zero
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "${(progress * 100).toInt()}%",
                fontWeight = FontWeight.Bold
            )
            Text(
                "Net",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

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
            value = dashboard?.activeEvents?.toString() ?: "-",
            modifier = Modifier.weight(1f)
        )

        SmallStatCard(
            title = "Tickets",
            value = dashboard?.totalTicketsSold?.toString() ?: "-",
            modifier = Modifier.weight(1f)
        )

        SmallStatCard(
            title = "Payout",
            value = if (state.profile?.payoutEnabled == true)
                "ON" else "OFF",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SmallStatCard(
    title: String,
    value: String,
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
            Text(
                value,
                fontWeight = FontWeight.Bold
            )
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
    onEvent: (PartnerHomeEvent) -> Unit
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
                title = "Events",
                icon = { Icon(Icons.Default.Event, null) },
                onClick = {
                    onEvent(PartnerHomeEvent.MyEventsClicked)
                },
                modifier = Modifier.weight(1f)
            )

            ActionCircle(
                title = "Refund",
                icon = { Icon(Icons.Default.Payments, null) },
                onClick = {
                    onEvent(PartnerHomeEvent.RefundClicked)
                },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCircle(
                title = "Painting",
                icon = { Icon(Icons.Default.Group, null) },
                onClick = {
                    onEvent(PartnerHomeEvent.PaintingClicked)
                },
                modifier = Modifier.weight(1f)
            )

            ActionCircle(
                title = "Shipment",
                icon = { Icon(Icons.Default.LocalShipping, null) },
                onClick = {
                    onEvent(PartnerHomeEvent.ShipmentClicked)
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ActionCircle(
    title: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
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
                    modifier = Modifier.padding(14.dp)
                ) { icon() }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(title)
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
            Text("Gross : Rp ${report.grossRevenue.toLong()}")
            Text("Fee : Rp ${report.platformFees.toLong()}")
            Text("Gateway : Rp ${report.paymentGatewayFees.toLong()}")

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                "Net : Rp ${report.netRevenue.toLong()}",
                fontWeight = FontWeight.Bold
            )
        }
    }
}
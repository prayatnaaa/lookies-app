package com.prayatna.lookiesapp.presentation.artistDashboard

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith

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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
                actions = {
                    state.summary?.let { summary ->
                        if (summary.status == "approved") {
                            IconButton(onClick = {
                                navController.navigate("${NavigationRoutes.MERCHANT_CONVERSATION_LIST}/${summary.businessId}")
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Chat,
                                    contentDescription = "Inbox"
                                )
                            }
                            IconButton(onClick = {
                                navController.navigate("${NavigationRoutes.EDIT_MERCHANT_PROFILE}/${summary.businessId}")
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = "Settings"
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            state.summary?.let { summary ->
                if (summary.status == "approved") {
                    ExtendedFloatingActionButton(
                        onClick = {
                            navController.navigate("${NavigationRoutes.UPLOAD_PAINTING}/${summary.businessId}")
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape,
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        text = { Text("Upload Art") }
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading && state.summary == null -> {
                    DashboardLoadingState(modifier = Modifier.align(Alignment.Center))
                }

                state.errorMessage != null && state.summary == null -> {
                    DashboardErrorState(
                        message = state.errorMessage!!,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp)
                    )
                }

                else -> {
                    state.summary?.let { summary ->
                        AnimatedContent(
                            targetState = summary.status,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "dashboard_status_transition"
                        ) { status ->
                            when (status) {
                                "pending" -> PendingReviewScreen(
                                    onBackClick = { navController.popBackStack() }
                                )
                                "rejected" -> RejectedApplicationScreen(
                                    onBackClick = { navController.popBackStack() },
                                    onReapplyClick = {
                                        navController.navigate(NavigationRoutes.ARTIST_DASHBOARD) {
                                            popUpTo(NavigationRoutes.ARTIST_DASHBOARD) { inclusive = true }
                                        }
                                    }
                                )
                                else -> ArtistDashboardContent(
                                    state = state,
                                    navController = navController,
                                    businessId = summary.businessId
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Status screens
// ---------------------------------------------------------------------------

@Composable
fun PendingReviewScreen(
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animated icon badge
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.HourglassTop,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(52.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Application Under Review",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Your artist application is currently being reviewed by our team. " +
                    "This usually takes 1–3 business days. We'll notify you once a decision has been made.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
        )

        Spacer(modifier = Modifier.height(36.dp))

        PendingStatusStepCard()

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}

@Composable
private fun PendingStatusStepCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "What happens next?",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            PendingStep(number = "1", label = "Application submitted ✓")
            PendingStep(number = "2", label = "Admin review in progress…", isActive = true)
            PendingStep(number = "3", label = "Decision notification")
        }
    }
}

@Composable
private fun PendingStep(
    number: String,
    label: String,
    isActive: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = if (isActive) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            modifier = Modifier.size(28.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isActive) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// ---------------------------------------------------------------------------

@Composable
fun RejectedApplicationScreen(
    onBackClick: () -> Unit = {},
    onReapplyClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.errorContainer,
            modifier = Modifier.size(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(52.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Application Not Approved",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Unfortunately, your artist application did not meet our current criteria. " +
                    "You are welcome to review our guidelines and submit a new application.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
        )

        Spacer(modifier = Modifier.height(36.dp))

        RejectionInfoCard()

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onReapplyClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Brush,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reapply as Artist")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}

@Composable
private fun RejectionInfoCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Common reasons for rejection",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            val reasons = listOf(
                "Incomplete portfolio submission",
                "Portfolio does not meet quality standards",
                "Invalid or missing identity documents",
                "Duplicate account detected"
            )
            reasons.forEach { reason ->
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(vertical = 3.dp)
                ) {
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(end = 8.dp, top = 1.dp)
                    )
                    Text(
                        text = reason,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Approved dashboard content
// ---------------------------------------------------------------------------

@SuppressLint("DefaultLocale")
@Composable
fun ArtistDashboardContent(
    state: com.prayatna.lookiesapp.presentation.artistDashboard.state.ArtistDashboardUiState,
    navController: NavController,
    businessId: String
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            EarningsOverviewCard(
                onClick = {
                    navController.navigate(
                        "${NavigationRoutes.MONTHLY_FINANCE_LIST_SCREEN}/$businessId"
                    )
                },
                totalEarnings = formatRupiah(state.summary?.totalRevenue ?: 0.0),
                pendingPayout = formatRupiah(
                    state.pendingOrderSplits?.totalAmount?.toDouble() ?: 0.0
                )
            )
        }

        item {
            DashboardSection(title = "Performance") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Items Sold",
                        value = state.summary?.totalSoldPaintings?.toString() ?: "0",
                        icon = Icons.Outlined.MonetizationOn,
                        iconTint = Color(0xFF4CAF50),
                        containerColor = Color(0xFFE8F5E9)
                    )
                    DashboardStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Rating",
                        value = String.format("%.1f", state.summary?.avgRating ?: 0.0),
                        icon = Icons.Filled.Star,
                        iconTint = Color(0xFFFFB300),
                        containerColor = Color(0xFFFFF8E1)
                    )
                    DashboardStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Paintings",
                        textColor = MaterialTheme.colorScheme.primary,
                        value = state.summary?.totalPaintings?.toString() ?: "0",
                        icon = Icons.Outlined.Inventory2,
                        iconTint = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                }
            }
        }

        item {
            DashboardSection(title = "Overview") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardStatCard(
                        modifier = Modifier.weight(1f),
                        title = "This Month",
                        value = formatRupiah(state.summary?.currentMonthRevenue ?: 0.0),
                        icon = Icons.AutoMirrored.Outlined.TrendingUp,
                        iconTint = Color(0xFF2196F3),
                        containerColor = Color(0xFFE3F2FD)
                    )
                    DashboardStatCard(
                        modifier = Modifier.weight(1f),
                        title = "To Ship",
                        value = state.summary?.ordersToShip?.toString() ?: "0",
                        icon = Icons.Filled.LocalShipping,
                        iconTint = Color(0xFFFF7043),
                        containerColor = Color(0xFFFBE9E7)
                    )
                }
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
            DashboardSection(title = "Management") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DashboardActionItem(
                        title = "My Artworks",
                        subtitle = "Manage portfolio, pricing & stock",
                        icon = Icons.Filled.Brush,
                        onClick = {
                            navController.navigate("${NavigationRoutes.PERSONAL_PAINTING}/$businessId")
                        }
                    )
                    DashboardActionItem(
                        title = "Exhibition History",
                        subtitle = "Track event participation status",
                        icon = Icons.Filled.Event,
                        onClick = {
                            navController.navigate(
                                "${NavigationRoutes.EXHIBITION_HISTORY}/$businessId"
                            )
                        }
                    )
                    DashboardActionItem(
                        title = "Shipments & Orders",
                        subtitle = "Monitor and manage buyer shipments",
                        icon = Icons.Filled.LocalShipping,
                        onClick = {
                            navController.navigate(
                                "${NavigationRoutes.SHIPMENT_LIST}/$businessId"
                            )
                        }
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

// ---------------------------------------------------------------------------
// Shared / utility composables
// ---------------------------------------------------------------------------

@Composable
private fun DashboardSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        content()
    }
}

@Composable
private fun DashboardLoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Loading studio…", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun DashboardErrorState(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
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
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun BalanceLogCard(log: MerchantBalanceLog) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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

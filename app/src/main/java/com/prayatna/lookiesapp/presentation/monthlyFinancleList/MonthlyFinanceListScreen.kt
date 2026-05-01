package com.prayatna.lookiesapp.presentation.monthlyFinancleList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReport
import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReportFilterInput
import com.prayatna.lookiesapp.presentation.monthlyFinancleList.state.MonthlyFinanceEvent
import com.prayatna.lookiesapp.presentation.monthlyFinancleList.state.MonthlyFinanceUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyFinanceListScreen(
    state: MonthlyFinanceUiState,
    onEvent: (MonthlyFinanceEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Monthly Finance", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(MonthlyFinanceEvent.NavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Filter Section (Horizontal Scroll)
            FilterSection(state = state, onEvent = onEvent)

            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))

            // 2. Content Area (Loading, Empty, or List)
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (state.monthlyFinancialReports.isEmpty()) {
                    EmptyFinanceState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.monthlyFinancialReports) { report ->
                            FinanceReportCard(report = report)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSection(
    state: MonthlyFinanceUiState,
    onEvent: (MonthlyFinanceEvent) -> Unit
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var expandedItemType by remember { mutableStateOf(false) }

    val itemTypeOptions = listOf("All", "ticket", "event_registration", "painting")

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            Icon(
                imageVector = Icons.Default.FilterAlt,
                contentDescription = "Filters",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 4.dp)
            )
        }

        // 1. START DATE FILTER
        item {
            FilterChip(
                selected = state.filterStartDate != null,
                onClick = { showStartDatePicker = true },
                label = { Text(state.filterStartDate ?: "Start Date") },
                leadingIcon = {
                    Icon(Icons.Default.CalendarMonth, null, modifier = Modifier.size(16.dp))
                }
            )
        }

        // 2. END DATE FILTER
        item {
            FilterChip(
                selected = state.filterEndDate != null,
                onClick = { showEndDatePicker = true },
                label = { Text(state.filterEndDate ?: "End Date") },
                leadingIcon = {
                    Icon(Icons.Default.CalendarMonth, null, modifier = Modifier.size(16.dp))
                }
            )
        }

        // 3. ITEM TYPE FILTER (Dropdown)
        item {
            Box {
                FilterChip(
                    selected = state.filterItemType != null,
                    onClick = { expandedItemType = true },
                    label = { Text(state.filterItemType ?: "Item Type") }
                )
                DropdownMenu(
                    expanded = expandedItemType,
                    onDismissRequest = { expandedItemType = false }
                ) {
                    itemTypeOptions.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                val selectedType = if (type == "All") null else type
                                onEvent(MonthlyFinanceEvent.ItemTypeSelected(selectedType ?: ""))
                                expandedItemType = false
                            }
                        )
                    }
                }
            }
        }

        // 4. APPLY BUTTON
        item {
            FilledTonalButton(
                onClick = {
                    onEvent(
                        MonthlyFinanceEvent.GetMonthlyFinancialReport(
                            MonthlyFinancialReportFilterInput(
                                startDate = state.filterStartDate,
                                endDate = state.filterEndDate,
                                itemType = state.filterItemType,
                                eventId = state.filterEventId,
                                merchantAccountId = ""
                            )
                        )
                    )
                },
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text("Apply")
            }
        }
    }

    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onEvent(MonthlyFinanceEvent.StartDateSelected(millis.toFormattedDateString()))
                    }
                    showStartDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onEvent(MonthlyFinanceEvent.EndDateSelected(millis.toFormattedDateString()))
                    }
                    showEndDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

private fun Long.toFormattedDateString(): String {
    val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    formatter.timeZone = java.util.TimeZone.getTimeZone("UTC")
    return formatter.format(java.util.Date(this))
}
@Composable
private fun FinanceReportCard(report: MonthlyFinancialReport) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Card Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = report.reportMonth,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${report.totalOrders} Orders",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Financial Breakdown
            FinancialRowItem(label = "Gross Revenue", value = "Rp ${report.grossRevenue.toLong()}")
            FinancialRowItem(label = "Platform Fees", value = "- Rp ${report.platformFees.toLong()}")
            FinancialRowItem(label = "Payment Gateway", value = "- Rp ${report.paymentGatewayFees.toLong()}")

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))

            // Net Revenue (Highlighted)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Net Revenue",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Rp ${report.netRevenue.toLong()}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun FinancialRowItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun BoxScope.EmptyFinanceState() {
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Receipt,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No reports found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Try adjusting your filters or check back later after your events have completed.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
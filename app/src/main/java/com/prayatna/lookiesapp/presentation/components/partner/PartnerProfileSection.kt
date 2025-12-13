package com.prayatna.lookiesapp.presentation.components.partner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.presentation.partner.detailpartner.state.DetailPartnerUiModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun PartnerProfileSection(
    data: DetailPartnerUiModel,
    onPortofolioClick: () -> Unit,
    onDocumentClick: (String) -> Unit,
    isAdmin: Boolean = false
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            AsyncImage(
                model = data.logoUrl?.replace("http://172.21.179.110", "http://10.0.2.2"),
                contentDescription = data.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = data.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    if (isAdmin && data.status.isNotBlank()) {
                        StatusPill(text = data.status)
                    }
                }
                Text(
                    text = data.type,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        item { HorizontalDivider() }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                InfoRow(Icons.Outlined.LocationOn, data.locations.firstOrNull()?.name ?: "Location not set")
                InfoRow(Icons.Outlined.AccountCircle, "User ID: ${data.userId}")
                InfoRow(Icons.Outlined.DateRange, "Created: ${data.createdAt.toFormattedDate()}")

                if (!data.portofolioLink.isNullOrBlank()) {
                    InfoRowClickable(
                        icon = Icons.Outlined.Link,
                        text = "View Portfolio",
                        onClick = onPortofolioClick
                    )
                }

                if (!data.ktpOwnerUrl.isNullOrBlank()) {
                    InfoRowClickable(
                        icon = Icons.Outlined.DocumentScanner,
                        text = "View KTP Owner",
                        onClick = { onDocumentClick(data.ktpOwnerUrl) }
                    )
                }

                if (!data.businessLicenseUrl.isNullOrBlank()) {
                    InfoRowClickable(
                        icon = Icons.Outlined.DocumentScanner,
                        text = "View Business License",
                        onClick = { onDocumentClick(data.businessLicenseUrl) }
                    )
                }
            }
        }

        if (data.userBanks.isNotEmpty()) {
            item { HorizontalDivider() }
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Bank Accounts", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    data.userBanks.forEach { bank ->
                        InfoRow(Icons.Outlined.AccountBalance, "${bank.bankName} - ${bank.bankAccountNumber}")
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(text = text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun InfoRowClickable(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline
        )
    }
}

private fun kotlinx.datetime.Instant.toFormattedDate(): String {
    val localDateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDateTime.date.dayOfMonth}-${localDateTime.date.monthNumber}-${localDateTime.date.year}"
}

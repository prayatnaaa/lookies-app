package com.prayatna.lookiesapp.presentation.components.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.checkout.state.PaymentMethodUiState

val availableBanks = listOf("BRI", "BCA", "BNI", "MANDIRI", "PERMATA")

data class PaymentMethodItem(
    val method: PaymentMethodUiState,
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

@Composable
fun CheckoutPaymentMethodContent(
    selectedMethod: PaymentMethodUiState,
    selectedBankCode: String,
    onPaymentMethodSelected: (PaymentMethodUiState) -> Unit,
    onBankCodeSelected: (String) -> Unit
) {

    val methods = listOf(
        PaymentMethodItem(
            PaymentMethodUiState.QRIS,
            "QRIS",
            "Scan QR with any e-wallet / m-banking",
            Icons.Default.QrCode2
        ),
        PaymentMethodItem(
            PaymentMethodUiState.VA,
            "Virtual Account",
            "Transfer via bank virtual account",
            Icons.Default.AccountBalance
        ),
        PaymentMethodItem(
            PaymentMethodUiState.BANK,
            "Bank Transfer",
            "Manual bank transfer",
            Icons.Default.Money
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Text(
            text = "Payment method",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Select your payment method",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        methods.forEach { item ->
            PaymentItem(
                title = item.title,
                subtitle = item.subtitle,
                isSelected = selectedMethod == item.method,
                onClick = { onPaymentMethodSelected(item.method) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Expand bank picker when VA is selected
            if (item.method == PaymentMethodUiState.VA) {
                AnimatedVisibility(
                    visible = selectedMethod == PaymentMethodUiState.VA,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    BankPickerGrid(
                        selectedBankCode = selectedBankCode,
                        onBankSelected = onBankCodeSelected
                    )
                }
                if (selectedMethod == PaymentMethodUiState.VA) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun BankPickerGrid(
    selectedBankCode: String,
    onBankSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(12.dp)
    ) {
        Text(
            text = "Select Bank",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            // Fix height so we don't scroll inside scroll
            modifier = Modifier.heightIn(max = 200.dp)
        ) {
            items(availableBanks) { bank ->
                val isSelected = bank == selectedBankCode
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.5.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else MaterialTheme.colorScheme.surface
                        )
                        .clickable { onBankSelected(bank) }
                        .padding(vertical = 10.dp, horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = bank,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentItem(
    title: String,
    subtitle: String = "",
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    val borderColor = if (isSelected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.outlineVariant

    val backgroundColor = if (isSelected)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
    else
        MaterialTheme.colorScheme.surface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(color = borderColor, width = 1.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        icon()

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Icon(
            imageVector = if (isSelected)
                Icons.Default.RadioButtonChecked
            else
                Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
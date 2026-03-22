package com.prayatna.lookiesapp.presentation.components.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.checkout.state.PaymentMethodUiState

data class PaymentMethodItem(
    val method: PaymentMethodUiState,
    val title: String,
    val icon: ImageVector
)

@Composable
fun CheckoutPaymentMethodContent(
    selectedMethod: PaymentMethodUiState,
    onPaymentMethodSelected: (PaymentMethodUiState) -> Unit
) {

    val methods = listOf(
        PaymentMethodItem(PaymentMethodUiState.QRIS, "QRIS", Icons.Default.QrCode2),
        PaymentMethodItem(PaymentMethodUiState.BANK, "Bank", Icons.Default.Money)
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

        methods.forEach {
            PaymentItem(
                title = it.title,
                isSelected = selectedMethod == it.method,
                onClick = { onPaymentMethodSelected(it.method) },
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.title
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PaymentItem(
    title: String,
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

        Text(
            text = title,
            modifier = Modifier.weight(1f)
        )

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
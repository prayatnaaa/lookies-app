package com.prayatna.lookiesapp.presentation.components.merchant

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.merchant.MerchantMember

@Composable
fun MerchantMemberItem(
    modifier: Modifier = Modifier,
    isShowMemberName: Boolean = false,
    member: MerchantMember,
    showDivider: Boolean = true,
    showKycStatus: Boolean = true,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (isShowMemberName) {
                        member.username
                    } else {
                        member.tradingName ?: "Unknown Business"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Role: ${
                        member.role.replaceFirstChar { it.uppercase() }
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Status: ${
                        member.status.replaceFirstChar { it.uppercase() }
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (showKycStatus) {
                member.kycStatus?.let { status ->
                    Spacer(modifier = Modifier.width(12.dp))

                    KycStatusBadge(
                        status = status
                    )
                }
            }
        }

        if (showDivider) {
            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.8f)
            )
        }
    }
}


@Composable
private fun KycStatusBadge(
    status: String
) {
    val (containerColor, contentColor, label) = when (status.lowercase()) {
        "approved", "verified" -> Triple(
            Color(0xFFE8F5E9),
            Color(0xFF2E7D32),
            "Active"
        )

        "pending" -> Triple(
            Color(0xFFFFF8E1),
            Color(0xFFF57F17),
            "Pending"
        )

        "rejected" -> Triple(
            Color(0xFFFFEBEE),
            Color(0xFFC62828),
            "Rejected"
        )

        "not_started" -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            "Wait approval"
        )

        else -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            status.uppercase()
        )
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = containerColor
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 4.dp
            ),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}
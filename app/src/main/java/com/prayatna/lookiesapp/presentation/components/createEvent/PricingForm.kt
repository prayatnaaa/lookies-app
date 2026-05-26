package com.prayatna.lookiesapp.presentation.components.createEvent

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.CustomTextField
import com.prayatna.lookiesapp.presentation.components.registerBusiness.FormSectionCard

@Composable
fun PricingForm(
    isOnlineEvent: Boolean,
    isSelfExhibition: Boolean,
    ticketPrice: String?,
    onTicketPriceChange: (String?) -> Unit,
    artistRegistrationFee: String?,
    onArtistRegistrationFeeChange: (String?) -> Unit
) {
    FormSectionCard(
        title = "Pricing & Fees",
        icon = Icons.Default.Payments
    ) {
        Text(
            text = "Set fees for visitors and artists",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (!isOnlineEvent) {
            CustomTextField(
                value = ticketPrice ?: "",
                onValueChange = { input ->
                    val digitsOnly = input.filter { it.isDigit() }
                    onTicketPriceChange(digitsOnly)
                },
                label = "Visitor Ticket Price",
                keyboardType = KeyboardType.Number
            )
        } else {
            Text(
                text = "Online events are set to Free Entry for visitors.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (!isSelfExhibition) {
            if (!isOnlineEvent) Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = artistRegistrationFee ?: "",
                onValueChange = { input ->
                    val digitsOnly = input.filter { it.isDigit() }
                    onArtistRegistrationFeeChange(digitsOnly)
                },
                label = "Artist Registration Fee",
                keyboardType = KeyboardType.Number
            )
        }
    }
}
package com.prayatna.lookiesapp.presentation.components.createEvent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.presentation.components.CustomTextField
import com.prayatna.lookiesapp.utils.Helper

@Composable
fun PricingForm(
    ticketPrice: String?,
    onTicketPriceChange: (String?) -> Unit,
    artistRegistrationFee: String?,
    onArtistRegistrationFeeChange: (String?) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = "Pricing and fees",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
        Text(
            text = "This is the section about the pricing and fees",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        CustomTextField(
            value = Helper.formatIdr(ticketPrice ?: ""),
            onValueChange = { input ->
                val digitsOnly = input.filter { it.isDigit() }
                onTicketPriceChange(digitsOnly)
            },
            label = "Ticket price",
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(4.dp))

        CustomTextField(
            value = Helper.formatIdr(artistRegistrationFee ?: ""),
            onValueChange = { input ->
                val digitsOnly = input.filter { it.isDigit() }
                onArtistRegistrationFeeChange(digitsOnly)
            },
            label = "Artist registration fee",
            keyboardType = KeyboardType.Number
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

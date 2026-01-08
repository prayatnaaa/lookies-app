package com.prayatna.lookiesapp.presentation.components.detailevent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.utils.Constants

@Composable
fun DetailEventFooter(
    modifier: Modifier = Modifier,
    onBuyButtonClick: () -> Unit,
    showRegisterButton: Boolean = false,
    onRegisterButtonClick: () -> Unit = {},
    onSeePaintingsClick: () -> Unit = {},
    showBuyButton: Boolean = false,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(16.dp),
        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
    ) {
        Column {
            if (showRegisterButton) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = onRegisterButtonClick
                ) {
                    Text(text = "Register")
                }
            }

            if (showBuyButton) {
                Row(
                    modifier = modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    SeeArtsButton (
                        modifier = modifier.weight(0.5f),
                        onClick = onSeePaintingsClick
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    BuyButton(
                        modifier = modifier.weight(0.5f),
                        onClick = onBuyButtonClick
                    )
                }
            }
        }
    }
}

@Composable
fun BuyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier
            .width(150.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
    ) {
        Text(
            text = "Buy ticket",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SeeArtsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier
            .width(150.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        Text(
            text = "See all arts",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailEventFooterPreview() {
    DetailEventFooter(
        onBuyButtonClick = {},
    )
}

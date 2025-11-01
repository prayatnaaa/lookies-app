package com.prayatna.lookiesapp.presentation.components.detailevent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.ui.theme.Grey
import com.prayatna.lookiesapp.ui.theme.PureWhite

@Composable
fun DetailEventFooter(
    modifier: Modifier = Modifier,
    onBuyButtonClick: () -> Unit,
    onAddToCartButtonClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PureWhite,
            contentColor = BlackCharcoal,
            disabledContainerColor = Grey,
            disabledContentColor = PureWhite
        ),
        elevation = CardDefaults.cardElevation(16.dp),
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
    ) {
        Row(
            modifier = modifier
                .padding(16.dp)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {

            AddToCartButton(
                modifier = modifier.weight(0.5f),
                onClick = onAddToCartButtonClick
            )

            Spacer(modifier = Modifier.width(8.dp))

            BuyButton(
                modifier = modifier.weight(0.5f),
                onClick = onBuyButtonClick
            )
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
            containerColor = BlackCharcoal,
            contentColor = PureWhite,
            disabledContainerColor = Grey,
            disabledContentColor = PureWhite
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = "Buy",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun AddToCartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier
            .width(150.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = PureWhite,
            contentColor = BlackCharcoal,
            disabledContainerColor = Grey,
            disabledContentColor = PureWhite
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = BlackCharcoal)
    ) {
        Text(
            text = "Add to cart",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailEventFooterPreview() {
    DetailEventFooter(
        onBuyButtonClick = {},
        onAddToCartButtonClick = {}
    )
}

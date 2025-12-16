package com.prayatna.lookiesapp.presentation.components.partner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SaleOverviewCard(
    totalSales: String,
    totalRevenue: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Info(
                labelFor = "Total Sales",
                value = totalSales
            )
            Info(
                labelFor = "Total Revenue",
                value = totalRevenue
            )
        }
    }
}

@Composable
private fun Info(
    labelFor: String,
    value: String,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = labelFor,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = MaterialTheme.colorScheme.onPrimary,
            )
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value,
            fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 30.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun SaleOverviewCardPreview() {
    SaleOverviewCard(
        totalSales = "100",
        totalRevenue = "1000"
    )
}
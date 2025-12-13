package com.prayatna.lookiesapp.presentation.components.user.partnerapplication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoDivider(info: String) {
    Text(
        text = info.uppercase(),
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
}

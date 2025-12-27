package com.prayatna.lookiesapp.presentation.components.home

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.prayatna.lookiesapp.utils.Constants

@Composable
fun CategoryChip(label: String, isSelected: Boolean) {
    FilterChip(
        selected = isSelected,
        onClick = { },
        label = { Text(label) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE)
    )
}
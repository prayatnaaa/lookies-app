package com.prayatna.lookiesapp.presentation.components.registerEvent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.presentation.components.painting.PaintingCard
import com.prayatna.lookiesapp.presentation.registerEvent.state.RegisterEventEvent
import com.prayatna.lookiesapp.presentation.registerEvent.state.RegisterEventUiState

private data class CategoryStyle(
    val color: Color,
    val icon: ImageVector
)

private val categoryStyles = listOf(
    CategoryStyle(Color(0xFF6C63FF), Icons.Default.Palette),
    CategoryStyle(Color(0xFFE91E63), Icons.Default.Brush),
    CategoryStyle(Color(0xFF00BCD4), Icons.Default.ColorLens),
    CategoryStyle(Color(0xFFFF9800), Icons.Default.Draw),
    CategoryStyle(Color(0xFF4CAF50), Icons.Default.Palette),
    CategoryStyle(Color(0xFF9C27B0), Icons.Default.Brush),
    CategoryStyle(Color(0xFF795548), Icons.Default.ColorLens),
    CategoryStyle(Color(0xFF607D8B), Icons.Default.Draw),
)

@Composable
fun SelectPaintingContent(
    state: RegisterEventUiState,
    onEvent: (RegisterEventEvent) -> Unit
) {
    val groupedPaintings = remember(state.allPaintings) {
        state.allPaintings.groupBy { it.medium.ifBlank { "Other" } }
    }

    val categoryColorMap = remember(groupedPaintings) {
        groupedPaintings.keys.mapIndexed { index, key ->
            key to categoryStyles[index % categoryStyles.size]
        }.toMap()
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        groupedPaintings.forEach { (category, paintings) ->
            val style = categoryColorMap[category] ?: categoryStyles[0]

            item(key = "header_$category") {
                CategoryHeader(
                    category = category,
                    count = paintings.size,
                    accentColor = style.color,
                    icon = style.icon
                )
            }

            items(paintings, key = { it.id }) { painting: Painting ->
                val isSelected = painting.id in state.selectedIds
                val isLimitReached = state.selectedIds.size >= state.maxLimit
                val isClickable = isSelected || !isLimitReached

                CategoryPaintingCard(
                    painting = painting,
                    isSelected = isSelected,
                    accentColor = style.color,
                    onClick = {
                        if (isClickable) {
                            onEvent(RegisterEventEvent.TogglePainting(painting.id))
                        }
                    }
                )
            }

            item(key = "spacer_$category") {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun CategoryHeader(
    category: String,
    count: Int,
    accentColor: Color,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = accentColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(accentColor.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Text(
                text = "$count painting${if (count > 1) "s" else ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CategoryPaintingCard(
    painting: Painting,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    PaintingCard(
        modifier = Modifier,
        paintingUrl = painting.paintingUrl,
        name = painting.title,
        price = painting.price,
        isSelected = isSelected,
        artistName = "",
        onClick = onClick
    )
}
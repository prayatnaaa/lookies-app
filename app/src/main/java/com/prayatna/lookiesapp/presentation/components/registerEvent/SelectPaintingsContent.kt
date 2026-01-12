package com.prayatna.lookiesapp.presentation.components.registerEvent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.presentation.components.painting.PaintingCard
import com.prayatna.lookiesapp.presentation.registerEvent.state.RegisterEventEvent
import com.prayatna.lookiesapp.presentation.registerEvent.state.RegisterEventUiState

@Composable
fun SelectPaintingContent(
    state: RegisterEventUiState,
    onEvent: (RegisterEventEvent) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.allPaintings) { painting: Painting ->
            val isSelected = painting.id in state.selectedIds
            val isLimitReached = state.selectedIds.size >= state.maxLimit

            val isClickable = isSelected || !isLimitReached

            PaintingCard(
                modifier = Modifier,
                paintingUrl = painting.paintingUrl,
                name = painting.title,
                price = painting.price,
                isSelected = isSelected,
                artistName = "",
                onClick = {
                    if (isClickable) {
                        onEvent(RegisterEventEvent.TogglePainting(painting.id))
                    }
                }
            )
        }
    }
}
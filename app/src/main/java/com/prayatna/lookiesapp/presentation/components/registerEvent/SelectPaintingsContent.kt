package com.prayatna.lookiesapp.presentation.components.registerEvent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = state.allPaintings,
            key = { it.id }
        ) { painting: Painting ->

            val isSelected = painting.id in state.selectedIds
            val isLimitReached =
                state.selectedIds.size >= state.maxLimit

            val isClickable =
                isSelected || !isLimitReached

            PaintingCard(
                modifier = Modifier,
                paintingUrl = painting.paintingUrl,
                name = painting.title,
                price = painting.price,
                artistName = null,
                isSelected = isSelected,
                onClick = {
                    if (isClickable) {
                        onEvent(
                            RegisterEventEvent.TogglePainting(
                                painting.id
                            )
                        )
                    }
                }
            )
        }
    }
}
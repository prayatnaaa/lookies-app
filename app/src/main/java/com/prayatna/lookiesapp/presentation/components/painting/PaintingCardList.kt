package com.prayatna.lookiesapp.presentation.components.painting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.painting.Painting

@Composable
fun PaintingCardList(
    paintings: List<Painting>, onClick: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Adaptive(200.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(paintings.size) { painting ->
            PaintingCard(
                price = paintings[painting].price,
                paintingUrl = paintings[painting].paintingUrl.replace("http://172.21.179.110", "http://10.0.2.2"),
                name = paintings[painting].title,
                onClick = { onClick(paintings[painting].id) },
                status = paintings[painting].status
            )
        }
    }
}
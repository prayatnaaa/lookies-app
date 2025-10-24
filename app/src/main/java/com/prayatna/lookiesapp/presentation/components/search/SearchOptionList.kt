package com.prayatna.lookiesapp.presentation.components.search

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SearchOptionsGrid(
    modifier: Modifier = Modifier,
    items: List<Pair<String, ImageVector>>,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        items.chunked(2).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { item ->
                    SearchOptionTemplate(
                        title = item.first,
                        icon = item.second,
                        onClick = { onItemClick(item.first) },
                        modifier = modifier.weight(1f)
                    )
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = modifier.weight(1f))
                }
            }
            Spacer(modifier = modifier.height(8.dp))
        }
    }
}

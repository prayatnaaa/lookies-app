package com.prayatna.lookiesapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun CustomAsyncImage(
    model: String,
    contentDescription: String?,
    modifier: Modifier
) {
    AsyncImage(
        model = model
            .replace("http://172.21.179.110", "http://10.0.2.2"),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}
package com.prayatna.lookiesapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun CustomAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier
) {
    val finalModel = if (model is String) {
        model.replace("http://172.21.179.110", "http://10.0.2.2")
    } else {
        model
    }
    
    AsyncImage(
        model = finalModel,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}
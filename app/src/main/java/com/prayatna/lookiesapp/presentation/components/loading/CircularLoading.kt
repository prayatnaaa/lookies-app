package com.prayatna.lookiesapp.presentation.components.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CircularLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Color.Black.copy(alpha = 0.3f),
                shape = RoundedCornerShape(10.dp)
            ).wrapContentSize()
    ) {
        CircularProgressIndicator(
            modifier = modifier.size(50.dp),
            color = Color.White
        )
    }
}
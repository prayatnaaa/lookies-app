package com.prayatna.lookiesapp.presentation.loading

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.loading.SkeletonBox

@Composable
fun MainLoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        SkeletonBox(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        Spacer(Modifier.height(24.dp))

        repeat(3) {
            SkeletonBox(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth(0.7f)
            )
            Spacer(Modifier.height(16.dp))
        }

        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.weight(1f)) {
            items(6) {
                SkeletonBox(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(150.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

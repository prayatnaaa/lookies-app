package com.prayatna.lookiesapp.presentation.painting.detailpainting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading

@Composable
fun DetailPaintingScreen(
    navController: NavController,
    viewModel: DetailPaintingViewModel = hiltViewModel(),
    paintingId: Int
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(paintingId) {
        viewModel.loadDetailPainting(id = paintingId)
    }

    Scaffold(
        topBar = {
            BackTopBar(navController = navController)
        }
    ) { innerPadding ->
        when {
            uiState.painting != null -> {
                val data = uiState.painting
                data?.let { painting ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                            .fillMaxWidth()
                    ) {
                        AsyncImage(
                            contentDescription = null,
                            model = painting.paintingUrl.replace("http://172.21.179.110", "http://10.0.2.2"),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )

                        Text(text = painting.title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = painting.medium)
                    }
                }
            }

            uiState.isLoading -> {
                CircularLoading()
            }

            uiState.error != null -> {
                //TODO(): show dialog to refresh
            }
        }
    }
}
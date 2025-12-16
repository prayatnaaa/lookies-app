package com.prayatna.lookiesapp.presentation.painting.detailpainting

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.painting.DetailPaintingSection

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
        innerPadding.calculateTopPadding()
        when {
            uiState.painting != null -> {
                val data = uiState.painting
                data?.let { painting ->
                    DetailPaintingSection(
                        painting = painting,
                    )
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
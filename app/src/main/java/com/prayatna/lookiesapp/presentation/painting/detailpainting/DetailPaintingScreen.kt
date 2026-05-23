package com.prayatna.lookiesapp.presentation.painting.detailpainting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.painting.DetailPaintingSection
import com.prayatna.lookiesapp.presentation.painting.editpainting.navigateToEditPainting

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
            BackTopBar(
                onBackClick = {
                    navController.popBackStack()
                },
                title = "Detail Painting"
            )
        },
        floatingActionButton = {
            if (uiState.painting?.availabilityStatus == "available") {
                ExtendedFloatingActionButton(
                    onClick = { navController.navigateToEditPainting(paintingId) },
                    icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    text = { Text("Edit Artwork") },
                    containerColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                uiState.painting != null -> {
                    DetailPaintingSection(
                        painting = uiState.painting!!,
                    )
                }

                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularLoading()
                    }
                }

                uiState.error != null -> {
                    //TODO(): show dialog to refresh
                }
            }
        }
    }
}

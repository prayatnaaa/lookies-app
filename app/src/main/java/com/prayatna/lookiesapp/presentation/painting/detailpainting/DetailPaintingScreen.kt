package com.prayatna.lookiesapp.presentation.painting.detailpainting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    val snackbarHostState = remember { SnackbarHostState() }

    val snackbarMessage = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>("snackbar_message", null)
        ?.collectAsStateWithLifecycle()

    val shouldRefresh = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("refresh", false)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(snackbarMessage?.value) {
        snackbarMessage?.value?.let { message ->
            snackbarHostState.showSnackbar(message)
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("snackbar_message")
        }
    }

    LaunchedEffect(shouldRefresh?.value) {
        if (shouldRefresh?.value == true) {
            viewModel.loadDetailPainting(paintingId)
            navController.currentBackStackEntry?.savedStateHandle?.set("refresh", false)
        }
    }

    LaunchedEffect(paintingId) {
        viewModel.loadDetailPainting(id = paintingId)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

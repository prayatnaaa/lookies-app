package com.prayatna.lookiesapp.presentation.painting.paintinglist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.painting.PaintingCardList
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun PersonalPaintingListScreen(
    artistId: String,
    viewModel: PersonalPaintingListViewModel = hiltViewModel(),
    navController: NavController
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
            viewModel.init(artistId)
            navController.currentBackStackEntry?.savedStateHandle?.set("refresh", false)
        }
    }

    LaunchedEffect(artistId) {
        viewModel.init(artistId)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            BackTopBar(
                title = "My arts",
                onBackClick = {
                    navController.popBackStack()
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ElevatedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                onClick = {
                    navController.navigate("${NavigationRoutes.UPLOAD_PAINTING}/${artistId}")
                },
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(text = "Upload Painting",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                    )
            }
        },
        content = { contentPadding ->
            when {
                uiState.isLoading -> {
                    CircularLoading()
                }
                uiState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text("Error: ${uiState.error}")
                    }
                }
                else -> {
                    PaintingCardList(
                        modifier = Modifier
                            .padding(contentPadding),
                        paintings = uiState.paintings,
                        onClick = { id ->
                            navController.navigate("${NavigationRoutes.DETAIL_PAINTING}/$id")
                        }
                    )
                }
            }
        }
    )
}

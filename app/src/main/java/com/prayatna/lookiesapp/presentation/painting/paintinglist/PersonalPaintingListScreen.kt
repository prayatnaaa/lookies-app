package com.prayatna.lookiesapp.presentation.painting.paintinglist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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

    LaunchedEffect(artistId) {
        viewModel.init(artistId)
    }

    Scaffold(
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
                    Text("Error: ${uiState.error}")
                }
                else -> {
                    PaintingCardList(
                        modifier = Modifier
                            .padding(contentPadding),
                        paintings = uiState.paintings,
                        onClick = { id ->
                            navController.navigate("${NavigationRoutes.DETAIL_PAINTING}/$id")
                        })
                }
            }
        }
    )
}
package com.prayatna.lookiesapp.presentation.painting.participantPaintingList

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.paintingSubmission.PaintingSubmissionCard
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun ParticipantPaintingListScreen(
    participantId: String,
    navController: NavController,
    viewModel: ParticipantPaintingListViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(participantId) {
        viewModel.loadPaintings(participantId = participantId)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

        ) {
            when {
                uiState.isLoading -> {
                    CircularLoading()
                }
                uiState.errorMessage != null -> {
                    // Show error message
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        if (uiState.eventPaintings.isNotEmpty()) {
//                            item {
//                                val participantInfo = uiState.eventPaintings.first().participant
//                                ParticipantHeaderSummary(participant = participantInfo)
//                            }

                            item {
                                Text(
                                    text = "Registered paintings (${uiState.eventPaintings.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }

                        if (uiState.eventPaintings.isEmpty()) {
                            item {
                                Text(text = "empty")
                            }
                        }

                        items(uiState.eventPaintings) { paintingItem ->
                            PaintingSubmissionCard(
                                item = paintingItem,
                                onClick = {
                                     navController.navigate("${NavigationRoutes.DETAIL_PAINTING}/${paintingItem.painting.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
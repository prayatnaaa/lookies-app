package com.prayatna.lookiesapp.presentation.event.detailevent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.detailevent.DetailEventFooter
import com.prayatna.lookiesapp.presentation.components.detailevent.DetailEventImage
import com.prayatna.lookiesapp.presentation.components.detailevent.DetailEventInfo
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.ui.theme.PureWhite

@Composable
fun DetailEventScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DetailEventViewModel = hiltViewModel(),
    eventId: String,
) {
    val detailEventState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getEvent(eventId)
    }

    Scaffold (
        bottomBar = {
            DetailEventFooter()
        },
        topBar = {
            BackTopBar(
                navController = navController
            )
        },
        content = {
            innerPadding ->
            LazyColumn(
                modifier = modifier
                    .padding(innerPadding)
                    .background(PureWhite)
            ) {
                when {
                    detailEventState.isLoading -> {
                        item {
                            Box(
                                modifier = modifier
                                    .fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularLoading()
                            }
                        }
                    }
                    detailEventState.info != null -> {
                        detailEventState.info?.let { data ->
                            item {
                                DetailEventImage(
                                    imageUrl = data.event.bannerImageUrl.toString()
                                )
                            }
                            item {
                                DetailEventInfo(
                                    event = data.event,
                                    detailEvent = data.detail,
                                )
                            }
                        }
                    }
                    detailEventState.errorMessage != null -> {
                        item {
                            Box(
                                modifier = modifier
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = detailEventState.errorMessage
                                        ?: "Unknown error occurred.",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = { viewModel.retry(eventId) }) {
                                    Text("Retry")
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.eventPainting.EventPaintingDetailContent
import com.prayatna.lookiesapp.presentation.components.eventPainting.PartnerShipmentActionBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun PartnerExhibitionPaintingDetailScreen(
    navController: NavController,
    eventPaintingId: String,
    viewModel: EventPaintingDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(eventPaintingId) {
        viewModel.getEventPaintingDetail(eventPaintingId)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BackTopBar(
                onBackClick = { navController.popBackStack() },
                title = "Artwork Detail"
            )
        },
        bottomBar = {
            state.data?.let { painting ->
                PartnerShipmentActionBar(
                    status = painting.status,
                    onManageShipment = {
                        navController.navigate(
                            NavigationRoutes.EXHIBITION_SHIPMENT + "/${painting.id}"
                        )
                    }
                )
            }
        }
    ) { innerPadding ->

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularLoading()
            }
            return@Scaffold
        }

        state.data?.let { painting ->
            EventPaintingDetailContent(
                painting = painting,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
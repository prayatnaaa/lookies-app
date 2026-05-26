package com.prayatna.lookiesapp.presentation.event.eventlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.event.eventlist.state.EventListEffect
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun EventListRoute(
    navController: NavController,
    viewModel: EventListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                EventListEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                is EventListEffect.NavigateToDetail -> {
                    navController.navigate("${NavigationRoutes.DETAIL_EVENT}/${effect.eventId}")
                }
            }
        }
    }

    EventListScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

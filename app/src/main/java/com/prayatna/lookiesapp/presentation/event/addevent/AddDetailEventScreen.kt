package com.prayatna.lookiesapp.presentation.event.addevent

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.addevent.AddDetailEventForm
import com.prayatna.lookiesapp.presentation.components.addevent.AddEventFooter
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun AddDetailEventScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AddEventViewModel
) {
    val detailEvent = viewModel.detailState.value
    val status = viewModel.eventStatus.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(status.value) {
        if (status.value is DataResult.Success) {
            navController.navigate(NavigationRoutes.MAIN) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true}
                launchSingleTop = true
            }
            snackBarHostState.showSnackbar(
                message = "Event added successfully",
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        } else if (status.value is DataResult.Error) {
            snackBarHostState.showSnackbar(
                message = (status.value as DataResult.Error).error,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = BlackCharcoal,
        bottomBar = {
            AddEventFooter(
                route = NavigationRoutes.ADD_DETAIL_EVENT,
                onNextButton = {},
                onBackButton = {
                    navController.popBackStack()
                },
                onSubmitButton = {
                    Log.d("ADD-EVENT", detailEvent.locationUrl)
                    viewModel.addEvent()
                },
            )
        },
        content = { innerPadding ->

            if (status.value is DataResult.Loading) {
                CircularLoading()
            }
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AddDetailEventForm(
                    locationUrlValue = detailEvent.locationUrl,
                    onLocationUrlChange = {
                        viewModel.setLocationUrl(it)
                    },
                    ticketQuantityValue = detailEvent.ticketQuantity.toString(),
                    onTicketQuantityChange = {
                        viewModel.setTicketQuantity(it.toInt())
                    },
                    startTimeValue = detailEvent.startTime,
                    onStartTimeChange = {
                        viewModel.setStartTime(it)
                    },
                    endTimeValue = detailEvent.endTime,
                    onEndTimeChange = {
                        viewModel.setEndTime(it)
                    }
                )
            }
        }
    )
}
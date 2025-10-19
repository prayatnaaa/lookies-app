package com.prayatna.lookiesapp.presentation.event.addevent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.addevent.AddDetailEventForm
import com.prayatna.lookiesapp.presentation.components.addevent.AddEventFooter
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun AddDetailEventScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AddEventViewModel
) {
    val detailEvent = viewModel.detailState.value
    Scaffold(
        containerColor = BlackCharcoal,
        bottomBar = {
            AddEventFooter(
                route = NavigationRoutes.ADD_DETAIL_EVENT,
                onNextButton = {},
                onBackButton = {
                    navController.navigate(NavigationRoutes.ADD_EVENT)
                },
                onSubmitButton = {},
            )
        },
        content = { innerPadding ->
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
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
import com.prayatna.lookiesapp.presentation.components.addevent.AddEventFooter
import com.prayatna.lookiesapp.presentation.components.addevent.AddEventForm
import com.prayatna.lookiesapp.presentation.components.addevent.AddImageBannerPlaceholder
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun AddEventScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AddEventViewModel
) {
    val event = viewModel.eventState.value
    Scaffold(
        containerColor = BlackCharcoal,
        topBar = {
            BackTopBar(navController = navController)
        },
        bottomBar = {
            AddEventFooter(
                route = NavigationRoutes.ADD_EVENT,
                onNextButton = {
                    navController.navigate(NavigationRoutes.ADD_DETAIL_EVENT)
                },
                onBackButton = {},
                onSubmitButton = {},
            )
        },
        content = { innerPadding ->
            Column(modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AddImageBannerPlaceholder {  }
                AddEventForm(
                    titleValue = event.title,
                    onTitleChange = {
                        viewModel.setTitle(it)
                    },
                    locationValue = event.location,
                    onLocationChange = {
                        viewModel.setLocation(it)
                    },
                    ticketPriceValue = event.ticketPrice.toString(),
                    onTicketPriceChange = {
                        viewModel.setTicketPrice(it.toDouble())
                    },
                    registrationFee = event.registrationFee.toString(),
                    onRegistrationFeeChange = {
                        viewModel.setRegistrationFee(it.toDouble())
                    },
                    dateValue = event.date,
                    onDateChange = {
                        viewModel.setDate(it)
                    }
                )
            }
        }
    )
}
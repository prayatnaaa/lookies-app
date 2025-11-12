package com.prayatna.lookiesapp.presentation.user.partnerapplication.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.location.AddLocationTextField
import com.prayatna.lookiesapp.presentation.components.user.partnerapplication.PartnerApplicationFooter
import com.prayatna.lookiesapp.presentation.user.partnerapplication.PartnerApplicationViewModel
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun AddLocationScreen(
    navController: NavController,
    viewModel: PartnerApplicationViewModel
) {

    val formState by viewModel.addPartnerSubmissionFormState.collectAsStateWithLifecycle()
    Scaffold(
        bottomBar = {
            PartnerApplicationFooter(
                isButtonEnable = formState.locUrl.isNotEmpty() &&
                        formState.locName.isNotEmpty(),
                route = NavigationRoutes.ADD_LOCATION,
                onBackButton = { navController.popBackStack() },
                onProfileButton = {},
                onLocationButton = {
                    navController.navigate(NavigationRoutes.PARTNER_APPLICATION)
                },
                onSubmissionButton = {}
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AddLocationTextField(
                    value = formState.locName,
                    onValueChange = viewModel::onLocNameChange,
                    label = "Location name"
                )
                Spacer(modifier = Modifier.height(16.dp))
                AddLocationTextField(
                    value = formState.locUrl,
                    onValueChange = viewModel::onLocUrlChange,
                    label = "Location URL"
                )
            }
        }
    )
}
package com.prayatna.lookiesapp.presentation.user.partnerapplication

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.user.partnerapplication.PartnerApplicationCard

@Composable
fun PartnerApplicationScreen(
    viewModel: PartnerApplicationViewModel = hiltViewModel(),
    navController: NavController
) {

    val formState by viewModel.addPartnerSubmissionFormState.collectAsStateWithLifecycle()

    Scaffold(
        content = {
            it.calculateTopPadding()
            PartnerApplicationCard(
                value = formState,
                onPartnerChange = viewModel::onPartnerNameChanged,
                onTypeChange = viewModel::onPartnerTypeChange,
                onLocationChange = { location -> viewModel.onLocationIdChange(location.toIntOrNull() ?: 0) },
                onPortfolioChange = viewModel::onPortofolioChange
            )
        },
        bottomBar = {}
    )
}
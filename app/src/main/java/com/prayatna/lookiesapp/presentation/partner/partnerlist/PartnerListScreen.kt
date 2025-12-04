package com.prayatna.lookiesapp.presentation.partner.partnerlist

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.partner.PartnerListCard
import com.prayatna.lookiesapp.presentation.error.ErrorScreen
import com.prayatna.lookiesapp.ui.theme.PureWhite
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun PartnerListScreen(
    navController: NavController,
    viewModel: PartnerListViewModel = hiltViewModel()
) {

    val uiState by viewModel.partnerState.collectAsStateWithLifecycle()
    val roleState by viewModel.roleState.collectAsStateWithLifecycle()
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    val shouldRefresh = savedStateHandle
        ?.getStateFlow("shouldRefresh", false)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(shouldRefresh?.value) {
        if (shouldRefresh?.value == true) {
            viewModel.loadPartners()
            savedStateHandle["shouldRefresh"] = false
        }
    }

    when (val result = uiState) {
        is DataResult.Error -> {
            ErrorScreen(
                message = result.error,
                onRetry = { viewModel.loadPartners() }
            )
        }
        DataResult.Idle -> {}
        DataResult.Loading -> {
            Log.d("PartnerList", "Loading")
            CircularLoading()
        }
        is DataResult.Success -> {
            if (result.data.isEmpty()) {
                ErrorScreen(
                    message = "No partner found",
                    onRetry = { viewModel.loadPartners() }
                )
            } else {
                Scaffold(
                    containerColor = PureWhite,
                    content = { innerPadding ->
                        PartnerListCard(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            contentPadding = innerPadding,
                            onPartnerClick = {
                                navController.navigate("${NavigationRoutes.DETAIL_PARTNER}/${it.id}")
                            },
                            partnerList = result.data,
                            showStatus = roleState == "admin"
                        )
                    }
                )
            }
        }
    }
}
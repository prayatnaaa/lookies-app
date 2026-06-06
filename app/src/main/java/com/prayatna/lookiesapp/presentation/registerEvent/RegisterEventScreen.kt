package com.prayatna.lookiesapp.presentation.registerEvent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.registerEvent.BottomActionBar
import com.prayatna.lookiesapp.presentation.components.registerEvent.ConfirmPaintingsContent
import com.prayatna.lookiesapp.presentation.components.registerEvent.SelectPaintingContent
import com.prayatna.lookiesapp.presentation.registerEvent.state.RegisterEventEvent
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun RegisterEventScreen(
    viewModel: RegisterEventViewModel = hiltViewModel(),
    navController: NavController,
    eventId: Int,
    fee: Double,
    merchantId: String,
    maxPaintingPerArtist: Int
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    LaunchedEffect(eventId) {
        viewModel.onEvent(RegisterEventEvent.SetEventId(eventId))
        viewModel.onEvent(RegisterEventEvent.SetFee(fee))
        viewModel.onEvent(RegisterEventEvent.SetMerchantId(merchantId))
    }

    if (state.isSuccess) {
        state.successMessage?.let { message ->
            CustomBottomSheet (
                title = "Success!",
                message = "$message. Proceed to checkout to complete payment.",
                onConfirm = {
                    navController.navigate(
                        "${NavigationRoutes.CHECKOUT}/event_registration/${state.data?.data?.orderId}/1"
                    ) {
                        popUpTo(navController.currentBackStackEntry?.destination?.route ?: "") {
                            inclusive = true
                        }
                    }
                },
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }
    } else {
        state.errorMessage?.let { message ->
            CustomBottomSheet(
                title = "Error",
                message = message,
                onConfirm = {
                    viewModel.onEvent(RegisterEventEvent.DismissError)
                },
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }
    }

    Scaffold(
        topBar = {
            Column(Modifier.padding(16.dp).statusBarsPadding()) {
                Text(
                    text = if (state.currentStep == 1) "Step 1: Select painting" else "Step 2: Review & Submit",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Event Quota: ${state.selectedIds.size} / $maxPaintingPerArtist",
                    color = if (state.selectedIds.size > maxPaintingPerArtist) Color.Red else Color.Gray
                )
                LinearProgressIndicator(
                    progress = { if (state.currentStep == 1) 0.5f else 1f },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                )
            }
        },
        bottomBar = {
            BottomActionBar(state = state, onEvent = onEvent, onCancel = {
                navController.popBackStack()
            })
        }
    ) { padding ->

        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
            if (state.isLoading) {
                CircularLoading()
            }
            if (state.currentStep == 1) {
                SelectPaintingContent(state, onEvent)
            } else {
                ConfirmPaintingsContent(state, onEvent, fee)
            }
        }
    }
}
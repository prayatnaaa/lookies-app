package com.prayatna.lookiesapp.presentation.createPaintingReview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.createPaintingReview.state.PaintingReviewEffect

@Composable
fun CreatePaintingReviewRoute(
    viewModel: CreatePaintingReviewViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetTitle by remember { mutableStateOf("") }
    var bottomSheetMessage by remember { mutableStateOf("") }
    var isSuccessStatus by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PaintingReviewEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                is PaintingReviewEffect.ShowBottomDropdown -> {
                    bottomSheetTitle = effect.status
                    bottomSheetMessage = effect.message
                    isSuccessStatus = effect.status == "Success"
                    showBottomSheet = true
                }
            }
        }
    }

    CreatePaintingReviewScreen(
        uiState = state,
        onEvent = viewModel::onEvent
    )

    if (showBottomSheet) {
        CustomBottomSheet(
            title = bottomSheetTitle,
            message = bottomSheetMessage,
            onConfirm = {
                showBottomSheet = false
                if (isSuccessStatus) {
                    navController.popBackStack()
                }
            },
            onDismiss = {
                showBottomSheet = false
                if (isSuccessStatus) {
                    navController.popBackStack()
                }
            }
        )
    }
}
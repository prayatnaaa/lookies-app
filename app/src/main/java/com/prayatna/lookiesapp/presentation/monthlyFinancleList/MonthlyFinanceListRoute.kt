package com.prayatna.lookiesapp.presentation.monthlyFinancleList

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
import com.prayatna.lookiesapp.presentation.monthlyFinancleList.state.MonthlyFinanceEffect
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun MonthlyFinanceListRoute(
    navController: NavController,
    viewModel: MonthlyFinanceListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetTitle by remember { mutableStateOf("") }
    var bottomSheetMessage by remember { mutableStateOf("") }
    var isSuccessStatus by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                MonthlyFinanceEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                is MonthlyFinanceEffect.ShowToast -> {
                    bottomSheetTitle = effect.title
                    bottomSheetMessage = effect.message
                    isSuccessStatus = effect.title == "Success"
                    showBottomSheet = true
                }
                is MonthlyFinanceEffect.NavigateToWithdrawalList -> {
                    navController.navigate("${NavigationRoutes.MERCHANT_WITHDRAWAL_REQUEST_LIST}/${effect.businessId}")
                }

                is MonthlyFinanceEffect.NavigateToOrderDetail -> {
                    navController.navigate("${NavigationRoutes.PARTNER_ORDER_DETAIL}/${effect.orderId}")
                }
            }
        }
    }

    MonthlyFinanceListScreen(
        state = state,
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

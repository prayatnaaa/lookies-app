    package com.prayatna.lookiesapp.presentation.event.addevent

    import androidx.compose.runtime.remember
    import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
    import androidx.navigation.NavController
    import androidx.navigation.NavGraphBuilder
    import androidx.navigation.compose.composable
    import androidx.navigation.navigation
    import com.prayatna.lookiesapp.utils.NavigationRoutes

    const val EVENT_FLOW = "event_flow"

    fun NavGraphBuilder.eventNavGraph(navController: NavController) {
        navigation(
            startDestination = NavigationRoutes.ADD_EVENT,
            route = EVENT_FLOW
        ) {
            composable(NavigationRoutes.ADD_EVENT) { navBackStackEntry ->
                val mainEntry = remember(navBackStackEntry) {
                    navController.getBackStackEntry(EVENT_FLOW)
                }
                val viewModel: AddEventViewModel = hiltViewModel(mainEntry)
                AddEventScreen(viewModel = viewModel, navController = navController)
            }

            composable(NavigationRoutes.ADD_DETAIL_EVENT) { navBackStackEntry ->
                val mainEntry = remember(navBackStackEntry) {
                    navController.getBackStackEntry(EVENT_FLOW)
                }
                val viewModel: AddEventViewModel = hiltViewModel(mainEntry)
                AddDetailEventScreen(viewModel = viewModel, navController = navController)
            }
        }
    }
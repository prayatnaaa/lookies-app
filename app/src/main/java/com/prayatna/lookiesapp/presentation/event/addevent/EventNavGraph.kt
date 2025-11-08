    package com.prayatna.lookiesapp.presentation.event.addevent

    import androidx.compose.runtime.remember
    import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
    import androidx.navigation.NavController
    import androidx.navigation.NavGraphBuilder
    import androidx.navigation.compose.composable
    import androidx.navigation.navigation
    import com.prayatna.lookiesapp.utils.NavigationRoutes

    fun NavGraphBuilder.eventNavGraph(navController: NavController) {
        navigation(
            startDestination = NavigationRoutes.ADD_EVENT,
            route = NavigationRoutes.EVENT_FLOW
        ) {
            composable(NavigationRoutes.ADD_EVENT) { navBackStackEntry ->
                val mainEntry = remember(navBackStackEntry) {
                    navController.getBackStackEntry(NavigationRoutes.EVENT_FLOW)
                }
                val viewModel: AddEventViewModel = hiltViewModel(mainEntry)
                AddEventScreen(viewModel = viewModel, navController = navController)
            }

            composable(NavigationRoutes.ADD_DETAIL_EVENT) { navBackStackEntry ->
                val mainEntry = remember(navBackStackEntry) {
                    navController.getBackStackEntry(NavigationRoutes.EVENT_FLOW)
                }
                val viewModel: AddEventViewModel = hiltViewModel(mainEntry)
                AddDetailEventScreen(viewModel = viewModel, navController = navController)
            }
        }
    }
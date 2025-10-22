package com.prayatna.lookiesapp.presentation.admin.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.admin.AdminPanelCard
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun AdminMainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AdminMainViewModel = hiltViewModel()
) {

    val logoutStatus by viewModel.logoutStatus.collectAsStateWithLifecycle()

    LaunchedEffect(logoutStatus) {
        if (logoutStatus is DataResult.Success) {
            navController.navigate(NavigationRoutes.LOGIN) {
                popUpTo(NavigationRoutes.ADMIN_MAIN) {
                    inclusive = true
                }
            }
        }
    }
    Column (
        modifier = modifier.padding(16.dp)
    ) {
        AdminPanelCard(
            contentFor = "Artist Application",
            onClick = { }
        )
        Spacer(modifier = modifier.height(8.dp))

        AdminPanelCard(
            contentFor = "Event Application",
            onClick = {
                navController.navigate(NavigationRoutes.ADMIN_EVENT)
            }
        )
        Spacer(modifier = modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.logout()
            }
        ) {
            Text("Logout")
        }
    }
}
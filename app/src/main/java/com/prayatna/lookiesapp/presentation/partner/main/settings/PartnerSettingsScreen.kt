package com.prayatna.lookiesapp.presentation.partner.main.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.SharedViewModel
import com.prayatna.lookiesapp.presentation.components.CustomDialog
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun PartnerSettingsScreen(
    sharedViewModel: SharedViewModel,
    navController: NavController
) {

    val logoutStatus by sharedViewModel.logoutStatus.collectAsStateWithLifecycle()
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(logoutStatus) {
        if (logoutStatus is DataResult.Success) {
            navController.navigate(NavigationRoutes.LOGIN) {
                popUpTo(NavigationRoutes.PARTNER_MAIN_SCREEN) { inclusive = true }
            }
        } else if (logoutStatus is DataResult.Error) {
            isError = true
        }
    }

    if (isError) {
        CustomDialog(
            title = "Error",
            message = "Failed to logout",
            onDismiss = {
                isError = false
            },
            onConfirm = {
                isError = false
            }
        )
    }
    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                Button(
                    onClick = {
                        sharedViewModel.logout()
                    },
                    content = {
                        Text("Logout")
                    }
                )
            }
        }
    )
}
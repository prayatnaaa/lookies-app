package com.prayatna.lookiesapp.presentation.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.SharedViewModel
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.user.profile.ProfileCard
import com.prayatna.lookiesapp.presentation.components.user.profile.SettingsSection
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navController: NavController
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val logoutStatus by viewModel.logoutStatus.collectAsState()
    val profileState by sharedViewModel.profileState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.isError) {
        if (viewModel.isError) {
            snackBarHostState.showSnackbar(
                message = viewModel.errorMsg,
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
        }
    }

    LaunchedEffect(logoutStatus) {
        when (logoutStatus) {
            is DataResult.Success -> {
                navController.navigate(NavigationRoutes.LOGIN) {
                    popUpTo(NavigationRoutes.MAIN) { inclusive = true }
                }
            }

            is DataResult.Error -> {
                snackBarHostState.showSnackbar(
                    message = (logoutStatus as DataResult.Error).error,
                    duration = SnackbarDuration.Long,
                    withDismissAction = true
                )
            }

            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { padding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (profileState) {

                is DataResult.Success -> {
                    val profile = (profileState as DataResult.Success).data

                    ProfileCard(
                        username = profile.username ?: "Unknown",
                        onEditProfileClick = {
                            navController.navigate("${NavigationRoutes.EDIT_PROFILE}?isPartnerSignup=false")
                        }
                    )

                    Spacer(Modifier.height(4.dp))

                    SettingsSection(
                        title = "Become our partner",
                        onClick = {
                            navController.navigate("${NavigationRoutes.EDIT_PROFILE}?isPartnerSignup=true")
                        }
                    )

                    Button(onClick = { viewModel.logout() }) {
                        Text("Logout")
                    }
                }

                is DataResult.Loading -> CircularLoading()

                is DataResult.Error -> {
                    Text("Failed to load profile")
                }

                DataResult.Idle -> {  }
            }
        }
    }
}

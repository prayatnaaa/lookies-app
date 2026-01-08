package com.prayatna.lookiesapp.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.auth.AuthCard
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.register.events.RegisterEvent
import com.prayatna.lookiesapp.ui.theme.GreyTextLight
import com.prayatna.lookiesapp.ui.theme.PureWhite
import com.prayatna.lookiesapp.utils.Constants
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val registerStatus = viewModel.registerStatus.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {

                is RegisterEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Long,
                        withDismissAction = true
                    )
                }

                RegisterEvent.NavigateToLogin -> {
                    navController.navigate(NavigationRoutes.LOGIN) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { padding -> padding.calculateTopPadding()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .background(Constants.gradientBackground),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                text = "Welcome!",
                color = PureWhite,
                style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                ),
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                text = "Register to continue",
                color = GreyTextLight,
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                ),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthCard(
                title = "Lookies",
                onRegister = { viewModel.onSignUp() },
                onLogin = {
                    navController.navigate(NavigationRoutes.LOGIN) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                inRegister = true,
                emailValue = viewModel.emailValue,
                passwordValue = viewModel.passwordValue,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange
            )
        }

        if (registerStatus.value is DataResult.Loading) {
            CircularLoading()
        }
    }
}
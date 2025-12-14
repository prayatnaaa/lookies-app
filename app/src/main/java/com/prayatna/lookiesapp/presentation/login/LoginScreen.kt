package com.prayatna.lookiesapp.presentation.login

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
import com.prayatna.lookiesapp.ui.theme.GreyTextLight
import com.prayatna.lookiesapp.ui.theme.PureWhite
import com.prayatna.lookiesapp.utils.Constants
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun LoginScreen(modifier: Modifier = Modifier,
                navController: NavController,
                viewModel: LoginViewModel = hiltViewModel(),
) {

    val loginStatus = viewModel.loginStatus.collectAsStateWithLifecycle()
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(loginStatus.value) {
        val status = loginStatus.value

        if (status is DataResult.Success) {
            val role = status.data.role

            when (role) {
                "admin" -> {
                    navController.navigate(NavigationRoutes.ADMIN_MAIN) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
                "user" -> {
                    navController.navigate(NavigationRoutes.MAIN){
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
                else -> {
                    navController.navigate(NavigationRoutes.PARTNER_MAIN_SCREEN){
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }

        } else if (status is DataResult.Error) {
            val errorMsg = status.error

            errorMsg.let {
                snackBarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Long,
                    withDismissAction = true
                )
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        modifier = modifier.fillMaxSize(),
        content = { padding -> padding.calculateTopPadding()
            Column(modifier = modifier
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
                    text = "Welcome back!",
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
                    text = "Login to continue",
                    color = GreyTextLight,
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 30.sp,
                    ),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(16.dp))

                val emailValue = viewModel.emailValue
                val passwordValue = viewModel.passwordValue
                AuthCard(
                    title = "Lookies",
                    onLogin = {
                        viewModel.onSignIn()
                    },
                    onRegister = { navController.navigate(NavigationRoutes.REGISTER) },
                    inRegister = false,
                    emailValue = emailValue,
                    passwordValue = passwordValue,
                    onEmailChange = { viewModel.onEmailChange(it) },
                    onPasswordChange = { viewModel.onPasswordChange(it) }
                )
            }
            if (loginStatus.value is DataResult.Loading) {
                CircularLoading()
            }
        }
    )
}
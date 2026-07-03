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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.CustomDialog
import com.prayatna.lookiesapp.presentation.components.auth.AuthCard
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.login.state.AuthEvent
import com.prayatna.lookiesapp.ui.theme.GreyTextLight
import com.prayatna.lookiesapp.ui.theme.PureWhite
import com.prayatna.lookiesapp.utils.Constants
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun LoginScreen(modifier: Modifier = Modifier,
                navController: NavController,
                viewModel: LoginViewModel,
) {

    val loginStatus = viewModel.loginStatus.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isErrorDialog by remember { mutableStateOf(false) }

    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.resetLoginState()
    }

    LaunchedEffect(errorMessage) {
        showDialog = errorMessage != null
    }

    LaunchedEffect(viewModel.eventFlow) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is AuthEvent.ShowError -> {
                    showDialog = true
                    dialogMessage = event.message
                    isErrorDialog = true
                }
            }
        }
    }


    Scaffold(
        modifier = modifier.fillMaxSize(),
        content = { padding -> padding.calculateTopPadding()
            Column(modifier = modifier
                .fillMaxSize()
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
                        fontSize = 20.sp,
                    ),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(16.dp))

                val emailValue = viewModel.emailValue
                val passwordValue = viewModel.passwordValue
                AuthCard(
                    modifier = Modifier
                        .imePadding(),
                    isRegister = false,
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
            if (showDialog) {
                CustomDialog(

                    title = if (isErrorDialog) "Error" else "Success",
                    onDismiss = {
                        showDialog = false
                        dialogMessage = ""
                    },
                    message = dialogMessage,
                    onConfirm = {
                        showDialog = false
                        dialogMessage = ""
                        if (isErrorDialog) {
                            viewModel.resetLoginState()
                        }
                    }
                )
            }

            if (showDialog && errorMessage != null) {
                CustomDialog(
                    title = "Error",
                    message = errorMessage!!,
                    onDismiss = {
                        showDialog = false
                        dialogMessage = ""
                        viewModel.clearError()
                    },
                    onConfirm = {
                        showDialog = false
                        dialogMessage = ""
                        viewModel.clearError()
                    }
                )
            }

            if (loginStatus.value is DataResult.Loading) {
                CircularLoading()
            }
        }
    )
}
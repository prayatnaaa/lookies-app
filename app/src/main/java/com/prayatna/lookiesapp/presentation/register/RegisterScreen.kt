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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
    val registerStatus = viewModel.registerStatus.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isErrorDialog by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {

                is RegisterEvent.ShowSuccessDialog -> {
                    dialogMessage = event.message
                    isErrorDialog = false
                    showDialog = true
                }

                is RegisterEvent.ShowErrorDialog -> {
                    dialogMessage = event.message
                    isErrorDialog = true
                    showDialog = true
                }
            }
        }
    }

    Scaffold { padding -> padding.calculateTopPadding()
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

        if (showDialog) {
            AlertDialog(
                containerColor = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium,
                textContentColor = MaterialTheme.colorScheme.onPrimary,
                onDismissRequest = {},
                title = {
                    Text(if (isErrorDialog) "Error" else "Success",
                        color = MaterialTheme.colorScheme.onPrimary)
                },
                text = {
                    Text(dialogMessage, color = MaterialTheme.colorScheme.onPrimary)
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        if (!isErrorDialog) {
                            navController.navigate(NavigationRoutes.LOGIN) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }) {
                        Text("OK", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        }

        if (registerStatus.value is DataResult.Loading) {
            CircularLoading()
        }
    }
}
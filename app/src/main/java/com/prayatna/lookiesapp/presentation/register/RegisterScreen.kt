package com.prayatna.lookiesapp.presentation.register

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.auth.AuthCard
import com.prayatna.lookiesapp.ui.theme.light_onPrimary
import com.prayatna.lookiesapp.utils.Constants

@Composable
fun RegisterScreen(modifier: Modifier = Modifier,
                   navController: NavController,
                   viewModel: RegisterViewModel = hiltViewModel()) {

    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

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

                Text(text = "Lookies", style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                    color = light_onPrimary
                ))

                Spacer(modifier = modifier.height(32.dp))

                val email = viewModel.email.collectAsState(initial = "")
                val password = viewModel.password.collectAsState(initial = "")
                AuthCard(
                    title = "Welcome",
                    onLogin = {
                        val test = viewModel.onSignUp()
                        Log.d("REGISTER-TEST", "$test")
                    },
                    onRegister = {},
                    inRegister = false,
                    nameValue = "",
                    emailValue = email.value,
                    passwordValue = password.value,
                    onNameChange = {},
                    onEmailChange = {
                        viewModel.onEmailChange(it)
                    },
                    onPasswordChange = {
                        viewModel.onPasswordChange(it)
                    }
                )
            }
        }
    )
}

@Preview
@Composable
fun RegisterScreenPreview() {
//    RegisterScreen()
}
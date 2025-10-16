package com.prayatna.lookiesapp.presentation.main.profile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.profile.ProfileCard
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
    ) {

    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.isError) {
        if (viewModel.isError) {
            viewModel.errorMsg.let {
                snackBarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Long,
                    withDismissAction = true
                )
            }
        }
    }

    Scaffold(modifier = modifier.fillMaxSize(),
        content = {
            padding ->
            Column(modifier = modifier
                .fillMaxSize()
                .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                    if (viewModel.isSuccess) {
                        val user = viewModel.user
                        Log.d("PROFILE-TEST", "$user")
                        ProfileCard(
                            username = user?.username as String,
                            onEditProfileClick = {
                                navController.navigate(
                                    NavigationRoutes.EDIT_PROFILE
                                )
                            }
                        )
                    }

                Spacer(modifier = modifier.height(4.dp))

                Button(
                    onClick = {
                        viewModel.logout()
                    }
                ) {
                    Text(text = "Logout")
                }
            }

            if (viewModel.isLoading) {
                CircularLoading()
            }
        }
    )
}
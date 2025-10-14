package com.prayatna.lookiesapp.presentation.editprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.profile.EditProfileCard
import com.prayatna.lookiesapp.presentation.components.profile.EditProfileImageCard
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.ui.theme.DarkGrey
import com.prayatna.lookiesapp.ui.theme.LightGrey
import com.prayatna.lookiesapp.ui.theme.PureWhite
import com.prayatna.lookiesapp.utils.DataResult

@Composable
fun EditProfileScreen (
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {

    val status = viewModel.editProfileStatus.collectAsStateWithLifecycle()
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect (status.value) {
        val result = status.value

        if (result is DataResult.Success) {
            val message = result.data
            snackBarHostState.showSnackbar(
                message = "Success edit profile! $message",
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )

        } else if (result is DataResult.Error) {
            val errorMsg = result.error
            snackBarHostState.showSnackbar(
                message = errorMsg,
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
        }
    }

    Scaffold (
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back"
                    )
                }

                Text(
                    text = "Profile",
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        },
        content = { innerPadding ->

            Column (
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                if (status.value is DataResult.Loading) {
                    CircularLoading()
                }

                EditProfileImageCard {  }

                EditProfileCard(
                    usernameValue = viewModel.usernameValue,
                    fullNameValue = viewModel.fullNameValue,
                    addressValue = viewModel.addressValue,
                    bioValue = viewModel.bioValue,
                    onUsernameChange = {
                        viewModel.onUsernameChange(it)
                    },
                    onFullNameChange = {
                        viewModel.onFullNameChange(it)
                    },
                    onAddressChange = {
                        viewModel.onAddressChange(it)
                    },
                    onBioChange = {
                        viewModel.onBioChange(it)
                    }
                )

                Spacer(modifier = modifier.height(4.dp))

                ElevatedButton (
                    onClick = {
                        viewModel.onEditProfile()
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonColors(
                        containerColor = BlackCharcoal,
                        contentColor = PureWhite,
                        disabledContentColor = LightGrey,
                        disabledContainerColor = DarkGrey
                    )
                ) {
                    Text(text = "Save")
                }
            }
        }
    )
}
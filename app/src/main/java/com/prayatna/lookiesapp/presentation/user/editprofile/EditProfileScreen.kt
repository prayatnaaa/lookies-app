package com.prayatna.lookiesapp.presentation.user.editprofile

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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
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
import com.prayatna.lookiesapp.presentation.SharedViewModel
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.user.partnerapplication.PartnerApplicationFooter
import com.prayatna.lookiesapp.presentation.components.user.profile.EditProfileCard
import com.prayatna.lookiesapp.presentation.components.user.profile.EditProfileImageCard
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.ui.theme.DarkGrey
import com.prayatna.lookiesapp.ui.theme.LightGrey
import com.prayatna.lookiesapp.ui.theme.PureWhite
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen (
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    isPartnerSignup: Boolean = false
) {

    val status = viewModel.editProfileStatus.collectAsStateWithLifecycle()
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

    val profileState by sharedViewModel.profileState.collectAsStateWithLifecycle()

    LaunchedEffect(profileState) {
        if (profileState is DataResult.Success) {
            val profile = (profileState as DataResult.Success).data
            viewModel.prefillProfile(
                username = profile.username.orEmpty(),
                fullName = profile.fullName.orEmpty(),
                address = profile.address.orEmpty(),
                bio = profile.bio.orEmpty()
            )
        }
    }

    LaunchedEffect(status.value) {
        when (val result = status.value) {
            is DataResult.Success -> {
                launch {
                    snackBarHostState.showSnackbar(
                        message = "Profile updated! ${result.data}",
                        duration = SnackbarDuration.Short
                    )
                }
                sharedViewModel.refreshProfile()
                if (isPartnerSignup) {
                    navController.navigate(NavigationRoutes.ADD_LOCATION) {
                        popUpTo(NavigationRoutes.EDIT_PROFILE) { inclusive = true }
                    }
                } else {
                    navController.popBackStack()
                }
            }
            is DataResult.Error -> {
                snackBarHostState.showSnackbar(
                    message = result.error,
                    duration = SnackbarDuration.Long
                )
            }
            else -> {}
        }
    }

    Scaffold (
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            if (isPartnerSignup) {
                PartnerApplicationFooter(
                    route = "${NavigationRoutes.EDIT_PROFILE}?isPartnerSignup=true",
                    onBackButton = { navController.popBackStack() },
                    onProfileButton = {
                        if (!viewModel.isChanged) {
                            navController.navigate(NavigationRoutes.ADD_LOCATION) {
                                popUpTo(NavigationRoutes.EDIT_PROFILE) { inclusive = true }
                            }
                        } else {
                            viewModel.onEditProfile()
                        }
                    },
                    onLocationButton = {},
                    onSubmissionButton = {},
                )
            }
        },
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

                if (!isPartnerSignup) {
                    Text(
                        text = "Profile",
                        modifier = Modifier.align(Alignment.Center),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                } else {
                    Text(
                        text = "Confirm your profile first!",
                        modifier = Modifier.align(Alignment.Center),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val editStatus = status.value
                if (editStatus is DataResult.Loading) CircularLoading()

                EditProfileImageCard { }

                EditProfileCard(
                    usernameValue = viewModel.usernameValue,
                    fullNameValue = viewModel.fullNameValue,
                    addressValue = viewModel.addressValue,
                    bioValue = viewModel.bioValue,
                    onUsernameChange = viewModel::onUsernameChange,
                    onFullNameChange = viewModel::onFullNameChange,
                    onAddressChange = viewModel::onAddressChange,
                    onBioChange = viewModel::onBioChange
                )


                Spacer(modifier = modifier.height(4.dp))

                if (!isPartnerSignup) {
                    ElevatedButton(
                        enabled = viewModel.isChanged,
                        onClick = {
                            viewModel.onEditProfile()
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = BlackCharcoal,
                            contentColor = PureWhite,
                            disabledContainerColor = DarkGrey,
                            disabledContentColor = LightGrey
                        )

                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    )
}
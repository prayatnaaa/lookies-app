package com.prayatna.lookiesapp.presentation.user.editprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.SharedViewModel
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.user.partnerapplication.PartnerApplicationFooter
import com.prayatna.lookiesapp.presentation.components.user.profile.EditProfileCard
import com.prayatna.lookiesapp.presentation.components.user.profile.EditProfileImageCard
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    isPartnerSignup: Boolean = false
) {
    val status = viewModel.editProfileStatus.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val profileState by sharedViewModel.profileState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

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
                        message = "Profile updated!",
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

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (isPartnerSignup) "Confirm Profile" else "Edit Profile",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
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
        }
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .imePadding()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp) //
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                EditProfileImageCard {
                    // TODO: Implementasi upload image di sini nanti
                }

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

                if (!isPartnerSignup) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { viewModel.onEditProfile() },
                        enabled = viewModel.isChanged && status.value !is DataResult.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        if (status.value is DataResult.Loading) {
                            CircularLoading(modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                text = "Save Changes",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            if (status.value is DataResult.Loading && isPartnerSignup) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularLoading()
                }
            }
        }
    }
}
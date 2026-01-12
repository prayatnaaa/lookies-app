package com.prayatna.lookiesapp.presentation.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SavedSearch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
    val scrollState = rememberScrollState()

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
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            when (profileState) {

                is DataResult.Success -> {
                    val profile = (profileState as DataResult.Success).data

                    ProfileCard(
                        username = profile.username ?: "Unknown",
                        onEditProfileClick = {
                            navController.navigate("${NavigationRoutes.EDIT_PROFILE}?isPartnerSignup=false")
                        }
                    )

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "Dashboard",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp, start = 4.dp)
                    )

                    if (profile.hasPartnerSub == false) {
                        SettingsSection(
                            title = "Become a Partner",
                            subtitle = "Join us and expand your reach",
                            icon = Icons.Default.Handshake,
                            onClick = {
                                navController.navigate(NavigationRoutes.ADD_LOCATION)
                            }
                        )
                    }

                    SettingsSection(
                        title = "My Artworks",
                        subtitle = if (profile.isArtist == true) "Manage your gallery portfolio" else "Start uploading your art",
                        icon = Icons.Default.Palette,
                        onClick = {
                            if (profile.isArtist == false) {
                                navController.navigate(NavigationRoutes.UPLOAD_PAINTING)
                            } else {
                                navController.navigate("${NavigationRoutes.PERSONAL_PAINTING}/${profile.id}")
                            }
                        }
                    )

                    if (profile.isArtist == true) {
                        SettingsSection(
                            title = "Event Submissions",
                            subtitle = "Track paintings registered to events",
                            icon = Icons.Default.Event,
                            onClick = {
                               //TODO: add this route
                            }
                        )

                        SettingsSection(
                            title = "My Stats",
                            subtitle = "See statistics",
                            icon = Icons.Default.SavedSearch,
                            onClick = {
                                navController.navigate(NavigationRoutes.ARTIST_DASHBOARD)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.logout() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Log Out")
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                is DataResult.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularLoading()
                    }
                }

                is DataResult.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Failed to load profile",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                DataResult.Idle -> { }
            }
        }
    }
}
package com.prayatna.lookiesapp.presentation.artist.application

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.artist.ArtistApplicationCard
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.utils.DataResult

@Composable
fun ApplicationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ApplicationViewModel = hiltViewModel()
) {

    val applicationStatus = viewModel.applicationStatus.collectAsStateWithLifecycle()
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect (applicationStatus.value) {
        if (applicationStatus.value is DataResult.Success) {
            snackBarHostState.showSnackbar(
                message = "${applicationStatus.value}",
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
            navController.popBackStack()
        } else if (applicationStatus.value is DataResult.Error) {
            snackBarHostState.showSnackbar(
                message = "${applicationStatus.value}",
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
        }
    }

    Scaffold (
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back"
                    )
                }
            }
        },
        content = {
            if (applicationStatus.value is DataResult.Loading) {
                CircularLoading()
            }
            Column (
                modifier = modifier
                    .fillMaxSize()
                    .imePadding()
                    .padding(it)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ArtistApplicationCard(
                    businessTypeValue = viewModel.businessTypeValue,
                    portoUrlValue = viewModel.portoUrlValue,
                    motiveLetterValue = viewModel.motiveLetterValue,
                    onPortoChange = { portoValue ->
                        viewModel.onPortoChange(portoValue)
                    },
                    onMotiveLetterChange = { motiveLetterValue ->
                        viewModel.onMotiveLetterChange(motiveLetterValue)
                    },
                    onBusinessTypeChange = { businessTypeValue ->
                        viewModel.onBusinessTypeChange(businessTypeValue)
                    }
                )

                Spacer(modifier = modifier.height(16.dp))

                OutlinedButton(
                    modifier = modifier
                        .fillMaxWidth(),
                    onClick = {
                        viewModel.artistApplication()
                    }
                ) {
                    Text(text = "Submit")
                }
            }
        }
    )
}
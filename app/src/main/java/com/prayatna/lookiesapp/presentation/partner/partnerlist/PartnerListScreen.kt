package com.prayatna.lookiesapp.presentation.partner.partnerlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.partner.FilterItem
import com.prayatna.lookiesapp.presentation.components.partner.PartnerListCard
import com.prayatna.lookiesapp.presentation.error.ErrorScreen
import com.prayatna.lookiesapp.presentation.partner.partnerlist.state.MerchantBusinessType
import com.prayatna.lookiesapp.utils.Constants
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun PartnerListScreen(
    navController: NavController,
    viewModel: PartnerListViewModel = hiltViewModel()
) {

    val uiState by viewModel.partnerState.collectAsStateWithLifecycle()
    val roleState by viewModel.roleState.collectAsStateWithLifecycle()
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val filterState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    val shouldRefresh = savedStateHandle
        ?.getStateFlow("shouldRefresh", false)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(shouldRefresh?.value) {
        if (shouldRefresh?.value == true) {
            viewModel.loadPartners()
            savedStateHandle["shouldRefresh"] = false
        }
    }

    when (val result = uiState) {
        is DataResult.Error -> {
            ErrorScreen(
                message = result.error,
                onRetry = { viewModel.loadPartners() }
            )
        }
        DataResult.Idle -> {}
        DataResult.Loading -> {
            CircularLoading()
        }
        is DataResult.Success -> {
            if (result.data.isEmpty()) {
                ErrorScreen(
                    message = "No partner found",
                    onRetry = { viewModel.loadPartners() }
                )
            } else {
                Scaffold(
                    topBar = { BackTopBar(title = "Review merchants", onBackClick = {
                        navController.popBackStack()
                    }) },
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .background(MaterialTheme.colorScheme.background)
                            .statusBarsPadding()
                    ) {

                        OutlinedTextField(
                            value = filterState.title,
                            onValueChange = viewModel::onTitleChanged,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            placeholder = { Text("Search event...") },
                            singleLine = true,
                            shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            trailingIcon = {
                                if (filterState.title.isNotEmpty()) {
                                    IconButton(onClick = {
                                        viewModel.onTitleChanged("")
                                        viewModel.loadPartners()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    focusManager.clearFocus()
                                    viewModel.loadPartners()
                                }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))


                        // Top header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp)
                        ) {

                            FilterItem(
                                title = "All",
                                selected = filterState.selectedStatus == null,
                                onClick = {
                                    viewModel.onFilterSelected(null)
                                    viewModel.loadPartners()
                                }
                            )

                            FilterItem(
                                title = "Partner",
                                selected = filterState.selectedStatus == MerchantBusinessType.PARTNER,
                                onClick = { viewModel.onFilterSelected(MerchantBusinessType.PARTNER) }
                            )

                            FilterItem(
                                title = "Artist",
                                selected = filterState.selectedStatus == MerchantBusinessType.ARTIST,
                                onClick = { viewModel.onFilterSelected(MerchantBusinessType.ARTIST) }
                            )
                        }

                        // Partner list
                        PartnerListCard(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onPartnerClick = {
                                navController.navigate("${NavigationRoutes.DETAIL_PARTNER}/${it.id}")
                            },
                            partnerList = result.data,
                            showStatus = roleState == "admin"
                        )
                    }
                }
            }
        }
    }
}
package com.prayatna.lookiesapp.presentation.partner.detailpartner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.partner.PartnerProfileSection
import com.prayatna.lookiesapp.presentation.error.ErrorScreen
import com.prayatna.lookiesapp.ui.theme.DarkGreen
import com.prayatna.lookiesapp.ui.theme.Maroon

private enum class AdminAction { APPROVE, REJECT }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPartnerScreen(
    partnerId: String,
    viewModel: DetailPartnerViewModel = hiltViewModel(),
    onPortfolioClick: (String) -> Unit = {},
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val adminState by viewModel.adminState.collectAsStateWithLifecycle()
    val role by viewModel.roleState.collectAsStateWithLifecycle()

    // Bottom sheet state
    var pendingAction by remember { mutableStateOf<AdminAction?>(null) }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    var isResultSuccess by remember { mutableStateOf(false) }

    val confirmSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val resultSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(partnerId) {
        viewModel.loadPartnerDetail(partnerId)
    }

    // Handle success
    LaunchedEffect(adminState.success) {
        adminState.success?.let {
            pendingAction = null
            resultMessage = "Merchant has been ${it.status}"
            isResultSuccess = true
        }
    }

    // Handle error
    LaunchedEffect(adminState.error) {
        adminState.error?.let {
            pendingAction = null
            resultMessage = it
            isResultSuccess = false
        }
    }

    // ── Confirmation Bottom Sheet ──
    if (pendingAction != null) {
        val action = pendingAction!!
        val actionLabel = if (action == AdminAction.APPROVE) "Approve" else "Reject"
        val actionColor = if (action == AdminAction.APPROVE) DarkGreen else Maroon

        ModalBottomSheet(
            onDismissRequest = { pendingAction = null },
            sheetState = confirmSheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Confirm $actionLabel",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Are you sure you want to ${actionLabel.lowercase()} this partner?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { pendingAction = null },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }
                    ElevatedButton(
                        onClick = {
                            state.data?.let { data ->
                                if (action == AdminAction.APPROVE) {
                                    viewModel.approvePartner(data.id)
                                } else {
                                    viewModel.rejectPartner(data.id)
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = actionColor,
                            contentColor = Color.White
                        ),
                        enabled = !adminState.isLoading
                    ) {
                        if (adminState.isLoading) {
                            Text("Loading...")
                        } else {
                            Icon(
                                imageVector = if (action == AdminAction.APPROVE) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(actionLabel, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }

    // ── Result Bottom Sheet ──
    if (resultMessage != null) {
        ModalBottomSheet(
            onDismissRequest = {
                if (isResultSuccess) {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("shouldRefresh", true)
                    navController.navigateUp()
                }
                resultMessage = null
            },
            sheetState = resultSheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (isResultSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                    contentDescription = null,
                    tint = if (isResultSuccess) DarkGreen else Maroon,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = if (isResultSuccess) "Success" else "Failed",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = resultMessage!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                ElevatedButton(
                    onClick = {
                        if (isResultSuccess) {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("shouldRefresh", true)
                            navController.navigateUp()
                        }
                        resultMessage = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isResultSuccess) DarkGreen else Maroon,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (isResultSuccess) "Done" else "Dismiss",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    // ── Main content ──
    when {
        state.isLoading -> CircularLoading()

        state.error != null -> ErrorScreen(
            message = state.error!!,
            onRetry = { viewModel.loadPartnerDetail(partnerId) }
        )

        else -> {
            state.data?.let { data ->

                val showAdminButtons =
                    role == "admin"

                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        BackTopBar(
                           onBackClick = {
                               navController.popBackStack()
                           },
                            title = "Partner Details"
                        )
                    },
                    bottomBar = {
                        if (showAdminButtons) {
                            com.prayatna.lookiesapp.presentation.components.admin.AdminPartnerButtons(
                                onApprovedButtonClick = { pendingAction = AdminAction.APPROVE },
                                onRejectButtonClick = { pendingAction = AdminAction.REJECT }
                            )
                        }
                    }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        PartnerProfileSection(
                            data = data,
                            onPortofolioClick = {
                                data.websiteUrl?.let(onPortfolioClick)
                            },
                            isAdmin = showAdminButtons
                        )
                    }
                }
            }
        }
    }
}

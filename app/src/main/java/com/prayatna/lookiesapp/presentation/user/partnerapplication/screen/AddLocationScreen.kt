package com.prayatna.lookiesapp.presentation.user.partnerapplication.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.location.AddLocationTextField
import com.prayatna.lookiesapp.presentation.components.user.partnerapplication.InfoDivider
import com.prayatna.lookiesapp.presentation.components.user.partnerapplication.PartnerApplicationFooter
import com.prayatna.lookiesapp.presentation.components.user.partnerapplication.PartnerBankSection
import com.prayatna.lookiesapp.presentation.user.partnerapplication.PartnerApplicationViewModel
import com.prayatna.lookiesapp.presentation.user.partnerapplication.event.PartnerApplicationEvent
import com.prayatna.lookiesapp.ui.theme.LightGrey
import com.prayatna.lookiesapp.ui.theme.PureWhite
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun AddLocationScreen(
    navController: NavController,
    viewModel: PartnerApplicationViewModel
) {

    val formState by viewModel.form.collectAsStateWithLifecycle()
    val isValid = formState.locUrl.isNotEmpty() &&
            formState.locName.isNotEmpty() &&
            formState.bankName.isNotEmpty() &&
            formState.bankAccountNumber.isNotEmpty() &&
            formState.bankAccountHolder.isNotEmpty()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = PureWhite,
        bottomBar = {
            PartnerApplicationFooter(
                isButtonEnable = isValid,
                route = NavigationRoutes.ADD_LOCATION,
                onBackButton = { navController.popBackStack() },
                onProfileButton = {},
                onLocationButton = {
                    navController.navigate(NavigationRoutes.PARTNER_APPLICATION)
                },
                onSubmissionButton = {}
            )
        },
        topBar = {
            Column (modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){
                Text(
                    text = "Partner Application",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                )
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(color = LightGrey, thickness = 1.dp)
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                InfoDivider("Business location")
                AddLocationTextField(
                    value = formState.locName,
                    onValueChange = { viewModel.onEvent(PartnerApplicationEvent.LocNameChanged(it))},
                    label = "Location name",
                    placeholder = "Office"
                )
                Spacer(modifier = Modifier.height(8.dp))
                AddLocationTextField(
                    value = formState.locUrl,
                    onValueChange = { viewModel.onEvent(PartnerApplicationEvent.LocUrlChanged(it))},
                    label = "Location URL",
                    placeholder = "https://www.google.com"
                )
                Spacer(modifier = Modifier.height(16.dp))
                PartnerBankSection(
                    form = formState,
                    onEvent = viewModel::onEvent
                )
            }
        }
    )
}
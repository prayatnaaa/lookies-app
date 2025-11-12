package com.prayatna.lookiesapp.presentation.components.user.partnerapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.user.partnerapplication.state.PartnerSubmissionFormState

@Composable
fun PartnerApplicationCard(
    value: PartnerSubmissionFormState,
    modifier: Modifier = Modifier,
    onPartnerChange: (value: String) -> Unit,
    onTypeChange: (value: String) -> Unit,
    onPortfolioChange: (value: String) -> Unit,
    onLogoClick: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PartnerLogoSection(value.imageLogo, onClick = onLogoClick)
            Spacer(modifier = Modifier.padding(8.dp))
            PartnerApplicationTextField(
                label = "Partner Name",
                value = value.partnerName,
                onValueChange = onPartnerChange
            )
            PartnerApplicationTextField(
                label = "Partner Type",
                value = value.partnerType,
                onValueChange = onTypeChange
            )

            PartnerApplicationTextField(
                label = "Portfolio Link",
                value = value.portfolioLink,
                onValueChange = onPortfolioChange
            )
        }
    }
}
package com.prayatna.lookiesapp.presentation.components.user.partnerapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.user.partnerapplication.event.PartnerApplicationEvent
import com.prayatna.lookiesapp.presentation.user.partnerapplication.state.PartnerSubmissionFormState
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.ui.theme.PureWhite

@Composable
fun PartnerApplicationSection(
    form: PartnerSubmissionFormState,
    onEvent: (PartnerApplicationEvent) -> Unit,
    onPickLogo: () -> Unit,
    onPickKtp: () -> Unit,
    onPickBusinessLicense: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    ) {

        // ---------- LOGO ----------
        PartnerLogoSection(
            imageLogo = form.partnerLogo,
            onClick = { onPickLogo() }
        )

        Spacer(Modifier.padding(12.dp))

        // ---------- BASIC DETAILS ----------
        InfoDivider("basic details")

        PartnerApplicationTextField(
            label = "Partner Name",
            value = form.partnerName,
            placeholder = "Lookies Gallery",
            onValueChange = { onEvent(PartnerApplicationEvent.PartnerNameChanged(it)) }
        )

        PartnerApplicationTextField(
            label = "Partner Type",
            value = form.partnerType,
            placeholder = "Museum",
            onValueChange = { onEvent(PartnerApplicationEvent.PartnerTypeChanged(it)) }
        )

        PartnerApplicationTextField(
            label = "Portfolio Link",
            value = form.partnerPortfolioLink,
            placeholder = "https://www.lookiesgallery.com",
            onValueChange = { onEvent(PartnerApplicationEvent.PortfolioLinkChanged(it)) }
        )

        // ---------- LICENCES ----------
        InfoDivider("partner licences")

        FilePickerRow(
            label = "Upload KTP",
            fileName = form.ktpFile?.lastPathSegment ?: "Choose file...",
            onClick = { onPickKtp() }
        )

        FilePickerRow(
            label = "Upload Business License",
            fileName = form.businessLicenseFile?.lastPathSegment ?: "Choose file...",
            onClick = { onPickBusinessLicense() }
        )
    }
}

@Composable
private fun FilePickerRow(
    label: String,
    fileName: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
        ,
        colors = CardDefaults.elevatedCardColors(
            containerColor = PureWhite,
            contentColor = BlackCharcoal
        ),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label)
            Spacer(Modifier.height(6.dp))
            Text(fileName)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PartnerApplicationCardPreview() {
    PartnerApplicationSection(
        form = PartnerSubmissionFormState(),
        onEvent = {},
        onPickLogo = {},
        onPickKtp = {},
        onPickBusinessLicense = {}
    )
}

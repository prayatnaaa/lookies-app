package com.prayatna.lookiesapp.presentation.components.user.partnerapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.user.partnerapplication.event.PartnerApplicationEvent
import com.prayatna.lookiesapp.presentation.user.partnerapplication.state.PartnerSubmissionFormState

@Composable
fun PartnerBankSection(
    form: PartnerSubmissionFormState,
    onEvent: (PartnerApplicationEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        InfoDivider("bank details")

        Spacer(modifier = Modifier.height(12.dp))

        PartnerApplicationTextField(
            label = "Bank Name",
            value = form.bankName,
            placeholder = "e.g. Bank Central Asia (BCA)",
            onValueChange = { onEvent(PartnerApplicationEvent.BankNameChanged(it)) }
        )

        PartnerApplicationTextField(
            label = "Account Number",
            value = form.bankAccountNumber,
            placeholder = "e.g. 1234567890",
            // Jika komponen TextField Anda mendukung KeyboardOptions, tambahkan type Number disini
            onValueChange = { onEvent(PartnerApplicationEvent.BankAccountNumberChanged(it)) }
        )

        // Input 3: Nama Pemilik Rekening
        PartnerApplicationTextField(
            label = "Account Holder Name",
            value = form.bankAccountHolder,
            placeholder = "Name as shown in bank book",
            onValueChange = { onEvent(PartnerApplicationEvent.BankAccountHolderChanged(it)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PartnerBankSectionPreview() {
    PartnerBankSection(
        form = PartnerSubmissionFormState(),
        onEvent = {}
    )

}
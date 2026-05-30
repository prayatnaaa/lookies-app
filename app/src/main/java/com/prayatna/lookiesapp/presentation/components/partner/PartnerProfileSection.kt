package com.prayatna.lookiesapp.presentation.components.partner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.prayatna.lookiesapp.domain.model.admin.GetKycDocument
import com.prayatna.lookiesapp.domain.model.merchant.Member
import com.prayatna.lookiesapp.domain.model.merchant.MerchantAddress
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBankAccount
import com.prayatna.lookiesapp.domain.model.merchant.MerchantDetail
import com.prayatna.lookiesapp.domain.model.merchant.MerchantIndividual

@Composable
fun PartnerProfileSection(
    data: MerchantDetail,
    onPortofolioClick: () -> Unit,
    isAdmin: Boolean = false,
    kycDocuments: List<GetKycDocument> = emptyList(),
    onKycDocumentClick: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // ── HEADER ──
        item {
            ProfileHeader(data)
        }

        // ── NAME & TYPE ──
        item {
            Column(Modifier.padding(horizontal = 20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = data.legalName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    if (isAdmin && !data.kycStatus.isNullOrBlank()) {
                        Spacer(Modifier.width(8.dp))
                        StatusPill(text = data.kycStatus)
                    }
                }

                Text(
                    text = data.type,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        // ── ABOUT ──
        if (!data.description.isNullOrBlank()) {
            item {
                SectionCard("About") {
                    Text(
                        text = data.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // ── OWNER ──
        if (data.ownerName != null) {
            item {
                SectionCard("Owner") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PartnerAvatar(
                            pictureUrl = data.ownerPicture,
                            name = data.ownerName,
                            size = 48
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(data.ownerName, fontWeight = FontWeight.SemiBold)
                            data.ownerEmail?.let {
                                Text(it, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        // ── CONTACT ──
        item {
            SectionCard("Contact Information") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    data.email?.let {
                        InfoRow(Icons.Outlined.Email, "Email", it)
                    }
                    data.phoneNumber?.let {
                        InfoRow(Icons.Outlined.Phone, "Phone", it)
                    }
                    data.websiteUrl?.let {
                        InfoRowClickable(
                            Icons.Outlined.Link,
                            onPortofolioClick
                        )
                    }
                }
            }
        }

        // ── BUSINESS ──
        item {
            SectionCard("Business Details") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoRow(Icons.Outlined.Business, "Industry", data.industryCategory)
                    InfoRow(Icons.Outlined.Public, "Country", data.countryOfOperation)
                    data.dateOfRegistration?.let {
                        InfoRow(Icons.Outlined.CalendarToday, "Registered", it)
                    }
                    InfoRow(
                        Icons.Outlined.Business,
                        "Type",
                        data.merchantType.replaceFirstChar { it.titlecase() }
                    )
                }
            }
        }

        // ── ADDRESSES ──
        if (data.addresses.isNotEmpty()) {
            item {
                SectionCard("Addresses") {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        data.addresses.forEach {
                            AddressItem(it)
                        }
                    }
                }
            }
        }

        // ── BANK ──
        if (data.bankAccounts.isNotEmpty()) {
            item {
                SectionCard("Bank Accounts") {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        data.bankAccounts.forEach {
                            BankItem(it)
                        }
                    }
                }
            }
        }

        // ── MEMBERS ──
        if (data.members.isNotEmpty()) {
            item {
                SectionCard("Team Members") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        data.members.forEach {
                            MemberItem(it)
                        }
                    }
                }
            }
        }

        // ── INDIVIDUALS ──
        if (data.individuals.isNotEmpty()) {
            item {
                SectionCard("Legal / Individuals") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        data.individuals.forEach {
                            IndividualItem(it)
                        }
                    }
                }
            }
        }

        // ── KYC DOCUMENTS (ADMIN ONLY) ──
        if (isAdmin && kycDocuments.isNotEmpty()) {
            item {
                SectionCard("KYC Documents") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        kycDocuments.forEach { doc ->
                            KycDocumentItem(
                                doc = doc,
                                onClick = { onKycDocumentClick(doc.fileId) }
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(Modifier.height(20.dp)) }
    }
}

@Composable
fun KycDocumentItem(
    doc: GetKycDocument,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.UploadFile,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Column {
            Text(
                text = doc.type.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Tap to view document",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
fun SectionCard(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(
                MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(12.dp))
        content()
    }
}

@Composable
fun AddressItem(data: MerchantAddress) {
    Column {
        Text(
            listOfNotNull(
                data.streetLine1,
                data.streetLine2
            ).joinToString(", ")
        )
        Text(
            listOfNotNull(
                data.subDistrict,
                data.district,
                data.city,
                data.province
            ).joinToString(", "),
            style = MaterialTheme.typography.bodySmall
        )
        Text(data.country, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun BankItem(data: MerchantBankAccount) {
    var isVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                data.bankName ?: data.bankCode,
                fontWeight = FontWeight.SemiBold
            )

            if (data.isPrimary) {
                Text(
                    "PRIMARY",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = if (isVisible) data.accountNumber
                else "****${data.accountNumber.takeLast(4)}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(
                    imageVector = if (isVisible)
                        Icons.Default.VisibilityOff
                    else
                        Icons.Default.Visibility,
                    contentDescription = "Toggle visibility"
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            data.accountHolderName,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
fun MemberItem(data: Member) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = data.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = data.email,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = data.role,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = data.status,
                style = MaterialTheme.typography.bodySmall,
                color = when (data.status.lowercase()) {
                    "active" -> Color.Green
                    "inactive" -> Color.Gray
                    else -> Color.Red
                }
            )
        }
    }
}

@Composable
fun IndividualItem(data: MerchantIndividual) {
    Column {
        Text("${data.givenNames} ${data.surname}", fontWeight = FontWeight.SemiBold)
        data.role?.let { Text(it) }
        data.email?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun InfoRowClickable(icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(
            text = "Visit website",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
fun ProfileHeader(data: MerchantDetail) {
    Box(modifier = Modifier.fillMaxWidth()) {

        // ── Banner ──
        val bannerUrl = data.pictureUrl
            ?.replace("http://172.21.179.110", "http://10.0.2.2")

        if (bannerUrl.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    )
            )
        } else {
            SubcomposeAsyncImage(
                model = bannerUrl,
                contentDescription = data.legalName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        // ── Gradient overlay ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        // ── Avatar ──
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 20.dp, y = 40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .padding(3.dp)
        ) {
            PartnerAvatar(
                pictureUrl = data.pictureUrl,
                name = data.legalName,
                size = 84
            )
        }
    }
}

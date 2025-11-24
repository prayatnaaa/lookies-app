package com.prayatna.lookiesapp.presentation.components.partner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.presentation.partner.detailpartner.state.DetailPartnerUiModel
import com.prayatna.lookiesapp.presentation.partner.detailpartner.state.LocationUiModel
import com.prayatna.lookiesapp.presentation.partner.detailpartner.state.ProfileUiModel
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal

@Composable
fun PartnerProfileSection(
    data: DetailPartnerUiModel,
    onPortofolioClick: () -> Unit,
    isAdmin: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        AsyncImage(
            model = data.logoUrl,
            contentDescription = data.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
        )

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = data.name ?: "Unknown Partner",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = BlackCharcoal
                )
                if (isAdmin && !data.status.isNullOrBlank()) {
                    StatusPill(text = data.status)
                }
            }
            Text(
                text = data.type ?: "-",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            // About
            Column {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                val bio = data.profile?.bio
                if (!bio.isNullOrBlank()) {
                    Text(
                        text = bio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "No bio available",
                        style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            // Info Rows
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoRow(
                    icon = Icons.Outlined.LocationOn,
                    text = data.locations.firstOrNull()?.name ?: "Location not set"
                )

                InfoRow(
                    icon = Icons.Outlined.AccountCircle,
                    text = buildString {
                        append(data.profile?.fullName ?: "Unknown")
                        append(" (@")
                        append(data.profile?.username ?: "no_username")
                        append(")")
                    }
                )

                val portfolio = data.portfolioLink
                if (!portfolio.isNullOrBlank()) {
                    InfoRowClickable(
                        icon = Icons.Outlined.Link,
                        text = portfolio,
                        onClick = onPortofolioClick
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun InfoRowClickable(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DetailPartnerSectionPreview() {
    PartnerProfileSection(
        onPortofolioClick = {},
        isAdmin = true,
        data = DetailPartnerUiModel(
            id = 1,
            name = "Stark Industries",
            type = "Personal",
            logoUrl = "https://qevtkceidfnpyhiacbgh.supabase.co/storage/v1/object/public/partner_assets/partner-logos/e4146d94-1baf-40f2-b1b2-8ac41fbb3454.png",
            portfolioLink = "www.stark-industries.com",
            status = "pending",
            locations = listOf(
                LocationUiModel(
                    name = "Jakarta Pusat, Indonesia",
                    locUrl = "www.google.com"
                )
            ),
            profile = ProfileUiModel(
                id = "1",
                profileUrl = "www.google.com",
                username = "tonystark",
                fullName = "Tony Stark",
                address = "Jakarta",
                bio = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam.",
                hasPartnerSub = true,
            )
        )
    )
}
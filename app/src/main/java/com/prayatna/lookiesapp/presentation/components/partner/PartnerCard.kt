package com.prayatna.lookiesapp.presentation.components.partner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.prayatna.lookiesapp.domain.model.user.MerchantBusiness

@Composable
fun PartnerCard(
    data: MerchantBusiness,
    onClick: () -> Unit,
    showStatus: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PartnerAvatar(
                    pictureUrl = data.pictureUrl,
                    name = data.legalName,
                    size = 48
                )

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text(
                        text = data.legalName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "View detail",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            if (showStatus) {
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    StatusPill(text = data.status)
                }
            }
        }
    }
}

@Composable
fun PartnerAvatar(
    pictureUrl: String?,
    name: String,
    size: Int,
    modifier: Modifier = Modifier
) {
    val imageUrl = pictureUrl?.replace("http://172.21.179.110", "http://10.0.2.2")

    if (imageUrl.isNullOrBlank()) {
        DefaultAvatar(name = name, size = size, modifier = modifier)
    } else {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = name,
            modifier = modifier
                .size(size.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            error = {
                DefaultAvatar(name = name, size = size)
            }
        )
    }
}

@Composable
private fun DefaultAvatar(name: String, size: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        if (name.isNotBlank()) {
            Text(
                text = name.first().uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default avatar",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size((size / 2).dp)
            )
        }
    }
}
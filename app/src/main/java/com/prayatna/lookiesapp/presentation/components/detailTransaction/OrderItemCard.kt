package com.prayatna.lookiesapp.presentation.components.detailTransaction

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.transaction.TransactionItem
import com.prayatna.lookiesapp.utils.formatDate
import com.prayatna.lookiesapp.utils.formatRupiah

@Composable
fun OrderItemCard(item: TransactionItem) {

    val itemTypeLabel = when (item.itemType.lowercase()) {
        "event_registration" -> "Registration"
        "event_ticket" -> "Ticket"
        "painting" -> "Painting"
        else -> item.itemType.replaceFirstChar { it.uppercase() }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = item.details?.imageUrl
                    ?.replace("http://172.21.179.110", "http://10.0.2.2"),
                contentDescription = item.details?.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(82.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant
                    )
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {


                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = itemTypeLabel,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 4.dp
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.details?.title ?: "Item",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                item.details?.artistName?.let { artist ->
                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "by $artist",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item.details?.eventDate?.let { date ->
                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = formatDate(date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // PRICE SECTION
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "${item.quantity}x ${formatRupiah(item.unitPrice)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = formatRupiah(item.subtotal),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
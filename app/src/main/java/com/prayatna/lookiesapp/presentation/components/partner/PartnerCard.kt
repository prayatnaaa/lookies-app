package com.prayatna.lookiesapp.presentation.components.partner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.presentation.partner.partnerlist.state.PartnerUiModel

@Composable
fun PartnerCard(
    data: PartnerUiModel,
    onClick: () -> Unit,
    showStatus: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = data.logoUrl.replace("http://172.21.179.110", "http://10.0.2.2"),
                contentDescription = data.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                onError = {
                    it.result.throwable.printStackTrace()
                }
            )

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = data.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(8.dp))

            if (showStatus) {
                StatusPill(text = data.status)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PartnerListCardPreview() {
    val dummyList = listOf(
        PartnerUiModel(
            id = "info",
            name = "Stark Industries",
            logoUrl = "https://example.com/logo.png",
            status = "pending",
        ),
        PartnerUiModel(
            id = "saa",
            name = "Wayne Enterprises",
            logoUrl = "https://example.com/logo.png",
            status = "approved",
        ),
        PartnerUiModel(
            id = "kids",
            name = "Hello World",
            logoUrl = "https://example.com/logo.png",
            status = "rejected",
        )
    )

    PartnerListCard(partnerList = dummyList, onPartnerClick = {}, showStatus = true)
}
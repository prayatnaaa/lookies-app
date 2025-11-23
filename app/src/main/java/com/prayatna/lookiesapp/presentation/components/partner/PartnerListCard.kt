package com.prayatna.lookiesapp.presentation.components.partner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.partner.partnerlist.state.PartnerUiModel

@Composable
fun PartnerListCard(
    partnerList: List<PartnerUiModel>,
    onPartnerClick: (PartnerUiModel) -> Unit,
    modifier: Modifier = Modifier,
    showStatus: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(partnerList) { partnerData ->
            PartnerCard (
                data = partnerData,
                onClick = { onPartnerClick(partnerData) },
                showStatus = showStatus
            )
        }
    }
}
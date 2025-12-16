package com.prayatna.lookiesapp.presentation.components.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun EventListLoading(
    modifier: Modifier = Modifier,
    count: Int = 5,
    showTicketPrice: Boolean = true,
    showStatus: Boolean = false,
    showActions: Boolean = false
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(count) {
            EventCardSkeleton(
                showTicketPrice = showTicketPrice,
                showStatus = showStatus,
                showActions = showActions
            )
        }
    }
}

@Composable
fun EventCardSkeleton(
    modifier: Modifier = Modifier,
    showTicketPrice: Boolean = true,
    showStatus: Boolean = false,
    showActions: Boolean = false
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column {
            // Image skeleton
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    SkeletonBox(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(20.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    SkeletonBox(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(14.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    SkeletonBox(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(14.dp)
                    )

                    if (showTicketPrice) {
                        Spacer(modifier = Modifier.height(6.dp))
                        SkeletonBox(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .height(14.dp)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    if (showStatus) {
                        SkeletonBox(
                            modifier = Modifier
                                .height(18.dp)
                                .fillMaxWidth(0.25f)
                        )
                    }

                    if (showActions) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SkeletonBox(
                                modifier = Modifier.height(24.dp).fillMaxWidth(0.1f)
                            )
                            SkeletonBox(
                                modifier = Modifier.height(24.dp).fillMaxWidth(0.1f)
                            )
                        }
                    }
                }
            }
        }
    }
}
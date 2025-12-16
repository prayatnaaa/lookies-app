package com.prayatna.lookiesapp.presentation.partner.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.partner.ActionButton
import com.prayatna.lookiesapp.presentation.components.partner.EventCountCard
import com.prayatna.lookiesapp.presentation.components.partner.SaleOverviewCard
import com.prayatna.lookiesapp.utils.NavigationRoutes

data class CountInfo(
    val count: Int,
    val title: String
)

@Composable
fun PartnerHomeScreen(
    navController: NavController
) {

    //TODO: replace dummy to actual data
    val dummyCountInfo = listOf(
        CountInfo(count = 10, title = "Events"),
        CountInfo(count = 5, title = "Offers"),
        CountInfo(count = 3, title = "Bookings")
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = RoundedCornerShape(4.dp),
                onClick = {
                    navController.navigate(NavigationRoutes.CREATE_EVENT)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text(text = "+",
                    fontSize = 30.sp)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(dummyCountInfo) { info ->
                        EventCountCard(count = info.count, title = info.title)
                    }
                }
            }

            item {
                SaleOverviewCard(
                    totalSales = "-",
                    totalRevenue = "-"
                )
            }
            item {
                ActionButton(
                    onClick = {},
                    labelFor = "My events"
                )
                Spacer(modifier = Modifier.height(16.dp))
                ActionButton(
                    onClick = {},
                    labelFor = "My collaborators"
                )
            }
        }
    }
}

package com.prayatna.lookiesapp.presentation.main.search

import androidx.compose.foundation.layout.Box
import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Padding
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import com.prayatna.lookiesapp.presentation.components.search.SearchOptionsGrid
import com.prayatna.lookiesapp.utils.NavigationRoutes

object SearchScreenOptions {
    const val EVENTS = "Events"
    const val PAINTINGS = "Paintings"
    const val ARTISTS = "Artists"
    const val PARTNERS = "Partners"
}

@Composable
fun SearchScreen(navController: NavController) {
    val options = listOf(
        SearchScreenOptions.EVENTS to Icons.Default.Event,
        SearchScreenOptions.PAINTINGS to Icons.Default.Padding,
        SearchScreenOptions.ARTISTS to Icons.Default.PersonSearch,
        SearchScreenOptions.PARTNERS to Icons.Default.MeetingRoom,
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        content = { innerPadding ->
            innerPadding.calculateTopPadding()
            SearchOptionsGrid(items = options) { clickedItem ->
                when (clickedItem) {
                    SearchScreenOptions.EVENTS -> {
                        navController.navigate(NavigationRoutes.EVENT_LIST)
                    }

                    SearchScreenOptions.PAINTINGS -> {}
                    SearchScreenOptions.ARTISTS -> {}
                    SearchScreenOptions.PARTNERS -> {
                        navController.navigate(NavigationRoutes.PARTNER_LIST)
                    }
                }
            }
        }
    )
}

package com.prayatna.lookiesapp.presentation.main.search

import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Padding
import androidx.compose.material.icons.filled.PersonSearch
import com.prayatna.lookiesapp.presentation.components.search.SearchOptionsGrid
import com.prayatna.lookiesapp.utils.NavigationRoutes

object SearchScreenOptions {
    const val EVENTS = "Events"
    const val PAINTINGS = "Paintings"
    const val ARTISTS = "Artists"
}

@Composable
fun SearchScreen(navController: NavController) {
    val options = listOf(
        SearchScreenOptions.EVENTS to Icons.Default.Event,
        SearchScreenOptions.PAINTINGS to Icons.Default.Padding,
        SearchScreenOptions.ARTISTS to Icons.Default.PersonSearch,
    )

    SearchOptionsGrid (items = options) { clickedItem ->
        when(clickedItem) {
            SearchScreenOptions.EVENTS -> { navController.navigate(NavigationRoutes.EVENT_LIST) }
            SearchScreenOptions.PAINTINGS -> {}
            SearchScreenOptions.ARTISTS -> {}
        }
    }
}

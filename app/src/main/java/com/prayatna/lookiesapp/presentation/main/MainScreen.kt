package com.prayatna.lookiesapp.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prayatna.lookiesapp.presentation.main.home.HomeScreen
import com.prayatna.lookiesapp.presentation.main.inbox.InboxScreen
import com.prayatna.lookiesapp.presentation.main.profile.ProfileScreen
import com.prayatna.lookiesapp.presentation.main.search.SearchScreen
import com.prayatna.lookiesapp.ui.theme.light_onSecondaryContainer
import com.prayatna.lookiesapp.ui.theme.light_primaryContainer
import com.prayatna.lookiesapp.ui.theme.light_secondaryContainer
import com.prayatna.lookiesapp.utils.BottomNavItem

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {

    var selectedRoute by remember { mutableStateOf("home") }
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(selectedRoute = selectedRoute) { itemRoute ->
                selectedRoute = itemRoute
                navController.navigate(itemRoute) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    ) { innerPadding ->
        Content(modifier = modifier.padding(innerPadding), navController = navController)

    }
}

@Composable
fun Content(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier.padding(bottom = 12.dp)
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen()
        }

        composable(BottomNavItem.Search.route) {
            SearchScreen()
        }

        composable(BottomNavItem.Inbox.route) {
            InboxScreen()
        }

        composable(BottomNavItem.Starred.route) {
            SearchScreen()
        }

        composable(BottomNavItem.Profile.route) {
            ProfileScreen()
        }
    }
}

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    selectedRoute: String,
    onSelectRoute: (route: String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Inbox,
        BottomNavItem.Starred,
        BottomNavItem.Profile
    )

    NavigationBar(
        modifier = modifier
            .background(light_onSecondaryContainer)
            .fillMaxWidth(),
        containerColor = light_onSecondaryContainer
    ) {
        items.forEach { item ->
            val isSelected = selectedRoute == item.route

            NavigationBarItem(
                icon = {
                    when (item.route) {
                        BottomNavItem.Home.route -> Icon(
                            imageVector = if (isSelected) Icons.Filled.Home else Icons.Outlined.Home,
                            tint = if (isSelected) light_secondaryContainer else light_primaryContainer,
                            contentDescription = item.label
                        )

                        BottomNavItem.Search.route -> Icon(
                            imageVector = if (isSelected) Icons.Filled.Search else Icons.Outlined.Search,
                            tint = if (isSelected) light_secondaryContainer else light_primaryContainer,
                            contentDescription = item.label
                        )

                        BottomNavItem.Inbox.route -> Icon(
                            imageVector = if (isSelected) Icons.Filled.Inbox else Icons.Outlined.Inbox,
                            tint = if (isSelected) light_secondaryContainer else light_primaryContainer,
                            contentDescription = item.label
                        )

                        BottomNavItem.Starred.route -> Icon(
                            imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star,
                            tint = if (isSelected) light_secondaryContainer else light_primaryContainer,
                            contentDescription = item.label
                        )

                        BottomNavItem.Profile.route -> Icon(
                            imageVector = if (isSelected) Icons.Filled.Person else Icons.Outlined.Person,
                            tint = if (isSelected) light_secondaryContainer else light_primaryContainer,
                            contentDescription = item.label
                        )
                    }
                },
                selected = false,
                label = {
                    Text(text = item.label,
                        color = if (isSelected) light_secondaryContainer else light_primaryContainer)
                },
                onClick = { onSelectRoute(item.route) },
            )
        }
    }
}
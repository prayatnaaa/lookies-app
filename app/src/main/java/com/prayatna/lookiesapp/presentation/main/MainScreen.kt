package com.prayatna.lookiesapp.presentation.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.FloatingActionButton
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.prayatna.lookiesapp.data.model.User
import com.prayatna.lookiesapp.presentation.main.home.HomeScreen
import com.prayatna.lookiesapp.presentation.main.inbox.InboxScreen
import com.prayatna.lookiesapp.presentation.main.profile.ProfileScreen
import com.prayatna.lookiesapp.presentation.main.search.SearchScreen
import com.prayatna.lookiesapp.presentation.main.starred.StarredScreen
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.ui.theme.Grey
import com.prayatna.lookiesapp.ui.theme.PureWhite
import com.prayatna.lookiesapp.utils.BottomNavItem
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    user: User
) {
    var selectedRoute by remember { mutableStateOf("home") }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: BottomNavItem.Home.route
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            if (user.role == "artist") {
                Log.d("ROLE", "role artist")
                FloatingActionButton(
                    contentColor = PureWhite,
                    containerColor = BlackCharcoal,
                    onClick = {
                        navHostController.navigate(NavigationRoutes.ADD_EVENT)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(selectedRoute = currentRoute) { itemRoute ->
                selectedRoute = currentRoute
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
        Content(modifier =
            modifier.padding(innerPadding),
            navController = navController,
            navHostController = navHostController)

    }
}

@Composable
fun Content(modifier: Modifier = Modifier, navController: NavHostController, navHostController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier.padding(bottom = 12.dp)
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(navController = navHostController)
        }

        composable(BottomNavItem.Search.route) {
            SearchScreen(navController = navHostController)
        }

        composable(BottomNavItem.Inbox.route) {
            InboxScreen()
        }

        composable(BottomNavItem.Starred.route) {
            StarredScreen()
        }

        composable(BottomNavItem.Profile.route) {
            ProfileScreen(navController = navHostController)
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
            .background(BlackCharcoal)
            .fillMaxWidth(),
        containerColor = BlackCharcoal
    ) {
        items.forEach { item ->
            val isSelected = selectedRoute == item.route

            NavigationBarItem(
                icon = {
                    when (item.route) {
                        BottomNavItem.Home.route -> Icon(
                            imageVector = if (isSelected) Icons.Filled.Home else Icons.Outlined.Home,
                            tint = if (isSelected) PureWhite else Grey,
                            contentDescription = item.label
                        )

                        BottomNavItem.Search.route -> Icon(
                            imageVector = if (isSelected) Icons.Filled.Search else Icons.Outlined.Search,
                            tint = if (isSelected) PureWhite else Grey,
                            contentDescription = item.label
                        )

                        BottomNavItem.Inbox.route -> Icon(
                            imageVector = if (isSelected) Icons.Filled.Inbox else Icons.Outlined.Inbox,
                            tint = if (isSelected) PureWhite else Grey,
                            contentDescription = item.label
                        )

                        BottomNavItem.Starred.route -> Icon(
                            imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star,
                            tint = if (isSelected) PureWhite else Grey,
                            contentDescription = item.label
                        )

                        BottomNavItem.Profile.route -> Icon(
                            imageVector = if (isSelected) Icons.Filled.Person else Icons.Outlined.Person,
                            tint = if (isSelected) PureWhite else Grey,
                            contentDescription = item.label
                        )
                    }
                },
                selected = false,
                label = {
                    Text(text = item.label,
                        color = if (isSelected) PureWhite else Grey)
                },
                onClick = { onSelectRoute(item.route) },
            )
        }
    }
}
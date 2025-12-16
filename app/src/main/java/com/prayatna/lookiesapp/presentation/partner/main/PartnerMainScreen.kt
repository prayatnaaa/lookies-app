package com.prayatna.lookiesapp.presentation.partner.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.prayatna.lookiesapp.presentation.partner.main.home.PartnerHomeScreen
import com.prayatna.lookiesapp.utils.PartnerBottomNavItem

@Composable
fun PartnerMainScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        navBackStackEntry?.destination?.route ?: PartnerBottomNavItem.Home.route

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            PartnerBottomNavigationBar(
                selectedRoute = currentRoute,
                onSelectRoute = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        PartnerContent(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            navHostController = navHostController,
        )
    }
}

@Composable
fun PartnerContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navHostController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = PartnerBottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(PartnerBottomNavItem.Home.route) {
            PartnerHomeScreen(navController = navHostController)
        }

        composable(PartnerBottomNavItem.Chat.route) {
//            InboxScreen()
        }

        composable(PartnerBottomNavItem.Transaction.route) {
//            StarredScreen()
        }

        composable(PartnerBottomNavItem.Settings.route) {
//            ProfileScreen(
//                navController = navHostController,
//            )
        }
    }
}

@Composable
fun PartnerBottomNavigationBar(
    modifier: Modifier = Modifier,
    selectedRoute: String,
    onSelectRoute: (String) -> Unit
) {
    val items = listOf(
        PartnerBottomNavItem.Home,
        PartnerBottomNavItem.Chat,
        PartnerBottomNavItem.Transaction,
        PartnerBottomNavItem.Settings
    )

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEach { item ->
            val isSelected = selectedRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectRoute(item.route) },
                icon = {
                    if (item.selectedIcon != null
                        && item.unselectedIcon != null) {
                        Icon(
                            imageVector = if (isSelected) {
                                item.selectedIcon
                            } else {
                                item.unselectedIcon
                            },
                            contentDescription = item.label
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = if (isSelected) {
                                painterResource(id = item.selectedDrawableIcon!!)
                            } else {
                                painterResource(id = item.unselectedDrawableIcon!!)
                            },
                            contentDescription = item.label
                        )
                    }
                },
                label = {
                    Text(text = item.label)
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}


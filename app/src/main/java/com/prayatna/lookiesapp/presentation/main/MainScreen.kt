package com.prayatna.lookiesapp.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Person
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
import com.prayatna.lookiesapp.R
import com.prayatna.lookiesapp.presentation.SharedViewModel
import com.prayatna.lookiesapp.presentation.forumlist.ForumListRoute
import com.prayatna.lookiesapp.presentation.main.home.HomeScreen
import com.prayatna.lookiesapp.presentation.main.profile.ProfileScreen
import com.prayatna.lookiesapp.presentation.main.transactionList.TransactionListScreen
import com.prayatna.lookiesapp.utils.BottomNavItem

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel
) {
//    var selectedRoute by remember { mutableStateOf("home") }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: BottomNavItem.Home.route
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(selectedRoute = currentRoute) { itemRoute ->
//                selectedRoute = currentRoute
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
            navHostController = navHostController, sharedViewModel = sharedViewModel)

    }
}

@Composable
fun Content(modifier: Modifier = Modifier,
            navController: NavHostController,
            navHostController: NavHostController,
            sharedViewModel: SharedViewModel) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier.padding(bottom = 12.dp)
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(navController = navHostController)
        }

//        composable(BottomNavItem.Search.route) {
//            SearchScreen(navController = navHostController)
//        }

        composable(BottomNavItem.Inbox.route) {
            ForumListRoute(navController = navHostController)
        }

        composable(BottomNavItem.Transaction.route) {
            TransactionListScreen(navController = navHostController)
        }

        composable(BottomNavItem.Profile.route) {
            ProfileScreen(navController = navHostController, sharedViewModel = sharedViewModel)
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedRoute: String,
    onSelectRoute: (route: String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
//        BottomNavItem.Search,
        BottomNavItem.Inbox,
        BottomNavItem.Transaction,
        BottomNavItem.Profile
    )

    Column {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(8.dp)
//                .background(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//                            Color.Transparent,
//                            Color.Black.copy(alpha = 0.15f),
//                        )
//                    )
//                )
//        )

        NavigationBar(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars),
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            items.forEach { item ->
                val isSelected = selectedRoute == item.route

                NavigationBarItem(
                    icon = {
                        when (item.route) {
                            BottomNavItem.Home.route -> Icon(
                                imageVector = if (isSelected) Icons.Filled.Home else Icons.Outlined.Home,
                                tint = if (isSelected) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                contentDescription = item.label
                            )

//                            BottomNavItem.Search.route -> Icon(
//                                imageVector = if (isSelected) Icons.Filled.Search else Icons.Outlined.Search,
//                                tint = if (isSelected) MaterialTheme.colorScheme.onSurface
//                                else MaterialTheme.colorScheme.onSurfaceVariant,
//                                contentDescription = item.label
//                            )

                            BottomNavItem.Inbox.route -> Icon(
                                imageVector = if (isSelected) Icons.Filled.Inbox else Icons.Outlined.Inbox,
                                tint = if (isSelected) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                contentDescription = item.label
                            )

                            BottomNavItem.Transaction.route -> Icon(
                                painter = painterResource(
                                    if (isSelected) R.drawable.filled_transaction
                                    else R.drawable.outlined_list
                                ),
                                tint = if (isSelected) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp),
                            )

                            BottomNavItem.Profile.route -> Icon(
                                imageVector = if (isSelected) Icons.Filled.Person else Icons.Outlined.Person,
                                tint = if (isSelected) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                contentDescription = item.label
                            )
                        }
                    },
                    selected = isSelected,
                    label = {
                        Text(
                            text = item.label,
                            color = if (isSelected) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    onClick = { onSelectRoute(item.route) },
                )
            }
        }
    }
}
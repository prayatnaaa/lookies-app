package com.prayatna.lookiesapp.utils

object NavigationRoutes {
    const val MAIN = "main"
    const val REGISTER = "register"
    const val LOGIN = "login"
}

sealed class BottomNavItem(val route: String, val label: String) {
    data object Home: BottomNavItem(route = "home", label = "Home")
    data object Search: BottomNavItem(route = "search", label = "Item")
    data object Inbox: BottomNavItem(route = "inbox", label = "Inbox")
    data object Starred: BottomNavItem(route = "starred", label = "Starred")
    data object Profile: BottomNavItem(route = "profile", label = "Profile")
}
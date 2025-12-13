package com.prayatna.lookiesapp.utils

object NavigationRoutes {
    const val DETAIL_PAINTING = "detail_painting"
    const val MAIN = "main"
    const val REGISTER = "register"
    const val LOGIN = "login"
    const val EDIT_PROFILE = "edit_profile"
    const val ADD_EVENT = "add_event"
    const val ADD_DETAIL_EVENT = "add_detail_event"
    const val ADMIN_MAIN = "admin_main"
    const val ADMIN_EVENT = "admin_event"
    const val DETAIL_EVENT = "detail_event"
    const val UPLOAD_PAINTING = "upload_painting"
    const val PARTNER_APPLICATION = "partner_application"
    const val ADD_LOCATION = "add_location"
    const val PARTNER_LIST = "partner_list"
    const val DETAIL_PARTNER = "partner_detail"
    const val MAIN_LOADING = "main_loading"
    const val PERSONAL_PAINTING = "personal_painting"
}

sealed class BottomNavItem(val route: String, val label: String) {
    data object Home: BottomNavItem(route = "home", label = "Home")
    data object Search: BottomNavItem(route = "search", label = "Search")
    data object Inbox: BottomNavItem(route = "inbox", label = "Inbox")
    data object Starred: BottomNavItem(route = "starred", label = "Starred")
    data object Profile: BottomNavItem(route = "profile", label = "Profile")
}
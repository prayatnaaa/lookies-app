package com.prayatna.lookiesapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Dataset
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.prayatna.lookiesapp.R

object NavigationRoutes {
    const val CREATE_PAINTING_REVIEW = "create_painting_review"
    const val ADMIN_TRANSACTION_LIST_ROUTE = "admin_transaction_list"
    const val INSERT_EVENT_PAINTINGS_ROUTE = "insert_event_paintings_route"
    const val SHIPMENT_LIST = "shipment_list"
    const val MERCHANT_WITHDRAWAL_REQUEST_LIST = "merchant_withdrawal_request_list"
    const val CREATE_WITHDRAWAL_REQUEST = "create_withdrawal_request"
    const val QRIS_PAYMENT = "qris_payment"
    const val VA_PAYMENT = "va_payment"
    const val MERCHANT_MEMBER_LIST = "merchant_member_list"
    const val MERCHANT_MEMBER_BY_MERCHANT_ID_LIST = "merchant_member_by_merchant_id_list"
    const val INVITE_MERCHANT_MEMBER = "invite_merchant_member"
    const val PAYMENT = "payment"
    const val MESSAGES = "messages"
    const val ARTIST_DASHBOARD = "artist_dashboard"
    const val DETAIL_EVENT_PAINTING = "detail_event_painting"
    const val PARTNER_EVENT_MANAGE = "partner_event_manage"
    const val MANAGE_PAINTINGS = "manage_paintings"
    const val PARTNER_MAIN_SCREEN = "partner_main_screen"
    const val PARTICIPANT_LIST = "participant_list"
    const val DETAIL_PAINTING = "detail_painting"
    const val MAIN = "main"
    const val REGISTER = "register"
    const val REGISTER_EVENT = "register_event"
    const val LOGIN = "login"
    const val MONTHLY_FINANCE_LIST_SCREEN = "monthly_finance_list_screen"
    const val SEARCH = "search"
    const val CHECKOUT = "checkout"
    const val EDIT_PROFILE = "edit_profile"
    const val CREATE_EVENT = "add_event"
    const val ADMIN_MAIN = "admin_main"
    const val EVENT_LIST = "event_list"
    const val ADMIN_EVENT = "admin_event"
    const val EVENT_PAINTING_LIST = "event_painting_list"
    const val DETAIL_EVENT = "detail_event"
    const val SELF_EVENT_LIST = "self_event_list"
    const val UPLOAD_PAINTING = "upload_painting"
    const val PARTNER_APPLICATION = "partner_application"
    const val ARTIST_APPLICATION = "artist_application"
    const val ADD_LOCATION = "add_location"
    const val PARTNER_LIST = "partner_list"
    const val DETAIL_PARTNER = "partner_detail"
    const val MAIN_LOADING = "main_loading"
    const val PERSONAL_PAINTING = "personal_painting"
    const val EDIT_EVENT = "edit_event"
    const val DETAIL_TRANSACTION = "detail_transaction"
    const val CREATE_USER_ADDRESS = "create_user_address"
    const val FORUM_LIST = "forum_list"
    const val FORUM_CHANNEL_LIST = "forum_channel_list"
    const val FORUM_MESSAGES = "forum_messages"
    const val FORUM_MEMBERS = "forum_members"
    const val SHIPMENT_DETAIL = "shipment_detail"
    const val REFUND_LIST = "refund_list"
    const val CREATE_REFUND = "create_refund"
    const val ORDER_REFUNDS = "order_refunds"
    const val EXHIBITION_SHIPMENT = "exhibition_shipment"
    const val EXHIBITION_HISTORY = "exhibition_history"
    const val ARTIST_EXHIBITION_PAINTING_DETAIL = "artist_exhibition_painting_detail"
    const val PARTNER_EXHIBITION_PAINTING_DETAIL = "partner_exhibition_painting_detail"
    const val PARTNER_REFUND = "partner_refund"
    const val BARCODE_SCANNER = "barcode_scanner"
    const val ADMIN_WITHDRAWAL_LIST = "admin_withdrawal_list"
    const val ADMIN_WITHDRAWAL_DETAIL = "admin_withdrawal_detail"
    const val ADMIN_DETAIL_EVENT = "admin_detail_event"
    const val ADMIN_TRANSACTION_DETAIL = "admin_transaction_detail"
    const val EVENT_PAINTING_GALLERY = "event_painting_gallery"
    const val EDIT_PAINTING = "edit_painting"
    const val REFUND_DETAIL = "refund_detail"
    const val SELECT_PAYOUT_CHANNEL = "select_payout_channel"
    const val ACCEPT_PARTNER_INVITATION = "accept_partner_invitation"
    const val PARTNER_ORDER_DETAIL = "partner_order_detail"
}

sealed class BottomNavItem(val route: String, val label: String) {
    data object Home: BottomNavItem(route = "home", label = "Home")
    data object Search: BottomNavItem(route = "search", label = "Search")
    data object Inbox: BottomNavItem(route = "inbox", label = "Forum")
    data object Transaction: BottomNavItem(route = "transaction", label = "Purchase")
    data object Profile: BottomNavItem(route = "profile", label = "Profile")
}

sealed class PartnerBottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector?,
    val unselectedIcon: ImageVector?,
    val selectedDrawableIcon: Int? = null,
    val unselectedDrawableIcon: Int? = null
) {
    data object Home : PartnerBottomNavItem(
        route = "home",
        label = "Home",
        selectedIcon = Icons.Filled.Dataset,
        unselectedIcon = Icons.Outlined.Dataset
    )

    data object Chat : PartnerBottomNavItem(
        route = "chat",
        label = "Chat",
        selectedIcon = Icons.Filled.Inbox,
        unselectedIcon = Icons.Outlined.Inbox
    )

    data object Transaction : PartnerBottomNavItem(
        route = "transaction",
        label = "Transaction",
        selectedIcon = null,
        unselectedIcon = null,
        selectedDrawableIcon = R.drawable.filled_transaction,
        unselectedDrawableIcon = R.drawable.outlined_list
    )

    data object Settings : PartnerBottomNavItem(
        route = "settings",
        label = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}

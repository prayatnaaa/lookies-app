package com.prayatna.lookiesapp.presentation.chat.merchantConversationList

import androidx.navigation.NavController
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToMerchantConversationList(merchantId: String) {
    this.navigate("${NavigationRoutes.MERCHANT_CONVERSATION_LIST}/$merchantId")
}
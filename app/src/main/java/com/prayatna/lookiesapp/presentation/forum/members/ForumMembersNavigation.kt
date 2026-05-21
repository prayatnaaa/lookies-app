package com.prayatna.lookiesapp.presentation.forum.members

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToForumMembers(forumId: String) {
    this.navigate("${NavigationRoutes.FORUM_MEMBERS}/$forumId")
}

fun NavGraphBuilder.forumMembersNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.FORUM_MEMBERS}/{forumId}",
        arguments = listOf(
            navArgument("forumId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val forumId = backStackEntry.arguments?.getString("forumId") ?: ""
        ForumMembersRoute(
            forumId = forumId,
            onBackClick = { navController.popBackStack() }
        )
    }
}

package com.prayatna.lookiesapp.presentation

import androidx.navigation.ActivityNavigator

interface Destination {
    val route: String
    val title: String
}

object RegisterDestination: Destination {
    override val route = "signup"
    override val title = "Sign up"
}
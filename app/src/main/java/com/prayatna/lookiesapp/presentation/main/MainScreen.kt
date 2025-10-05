package com.prayatna.lookiesapp.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    CircularLoading()
}
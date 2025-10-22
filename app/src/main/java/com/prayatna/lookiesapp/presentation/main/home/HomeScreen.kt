package com.prayatna.lookiesapp.presentation.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.event.eventlist.EventListScreen

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController
){

    Scaffold(
        content = { innerPadding ->
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally){
                EventListScreen(navController = navController, modifier = modifier.padding(innerPadding))
            }
        }
    )
}
package com.example.afnews.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.afnews.ui.home.HomeDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.afnews.ui.favorite.FavoriteDestination
import com.example.afnews.ui.favorite.FavoriteScreen
import com.example.afnews.ui.home.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route
    ) {
        composable(HomeDestination.route) { HomeScreen() }
        composable(FavoriteDestination.route) { FavoriteScreen() }
    }
}
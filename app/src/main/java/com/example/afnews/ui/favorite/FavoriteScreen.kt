package com.example.afnews.ui.favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.afnews.R
import com.example.afnews.ui.navigation.NavigationDestination

object FavoriteDestination: NavigationDestination {
    override val route: String = "Favorite"
    override val icon: Int = R.drawable.ic_favorite_nav_bar
}

@Composable
fun FavoriteScreen() {
    Column {
        Text(text = "Favorite screen")
    }
}


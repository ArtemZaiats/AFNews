package com.example.afnews.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.afnews.R
import com.example.afnews.data.Article
import com.example.afnews.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route: String = "Home"
    override val icon: Int = R.drawable.ic_home_nav_bar
}

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val homeUiState by viewModel.uiState.collectAsState()
    val (queryString, onQueryStringChange) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchPanel(
            queryString = queryString,
            onQueryStringChange = onQueryStringChange,
            onSearchClick = {
                viewModel.searchNews(queryString)
                onQueryStringChange("")
            }
        )
        NewsList(homeUiState.newsList)
    }
}

@Composable
fun SearchPanel(
    queryString: String,
    onQueryStringChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        TextField(
            value = queryString,
            onValueChange = { onQueryStringChange(it) },
            placeholder = { Text(text = "Search for...") },
            modifier = Modifier
                .weight(1f),
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextButton(
            onClick = onSearchClick,
            enabled = queryString.isNotEmpty()
        ) {
            Text(text = "Search")
        }
    }
}

@Composable
fun SortPanel() {
    Row {
       Text(text = "Sort by: ")
    }
}

@Composable
fun NewsList(
    newsList: List<Article>,
//    onFavoriteClick: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp)
    ) {
        items(newsList) {
            NewsItem(article = it)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsItem(
    article: Article,
//    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            GlideImage(
                model = article.urlToImage,
                contentDescription = "news image",
                contentScale = ContentScale.Crop,
                failure = placeholder(R.drawable.ic_news),
                modifier = modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "save favorite",
                tint = Color.White,
                modifier = modifier
                    .align(Alignment.TopEnd)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            )
            Text(
                text = article.author ?: "",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                modifier = modifier
                    .align(Alignment.BottomStart)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            )
        }
        Text(
            text = article.title,
            modifier = modifier
                .padding(vertical = 8.dp, horizontal = 8.dp)
        )
    }
}
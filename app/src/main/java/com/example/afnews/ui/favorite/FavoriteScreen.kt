package com.example.afnews.ui.favorite

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.afnews.R
import com.example.afnews.data.Article
import com.example.afnews.data.ArticleEntity
import com.example.afnews.ui.home.HomeViewModel
import com.example.afnews.ui.navigation.NavigationDestination

object FavoriteDestination : NavigationDestination {
    override val route: String = "Favorite"
    override val icon: Int = R.drawable.ic_favorite_nav_bar
}

@Composable
fun FavoriteScreen(modifier: Modifier = Modifier, viewModel: FavoriteViewModel = hiltViewModel()) {
    val uiState by viewModel.favoriteUiState.collectAsState()
    val context = LocalContext.current

    if (uiState.newsList.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "save favorite",
                tint = Color.Gray,
                modifier = modifier.size(64.dp)
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = "There is nothing here...",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.Black
                )
            )
        }
    } else {
        SavedNewsList(
            newsList = uiState.newsList,
            viewModel = viewModel,
            context = context
        )
    }
}

@Composable
fun SavedNewsList(
    newsList: List<ArticleEntity>,
    viewModel: FavoriteViewModel,
    context: Context
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp)
    ) {
        items(newsList) {
            NewsItem(article = it, viewModel = viewModel, context = context)
        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsItem(
    article: ArticleEntity,
    viewModel: FavoriteViewModel,
    context: Context,
    modifier: Modifier = Modifier
) {

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, article.url)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)

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
                failure = placeholder(R.drawable.news_placeholder),
                modifier = modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            IconButton(
                modifier = modifier
                    .align(Alignment.TopEnd),
                onClick = { viewModel.deleteFromSaved(article) }
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "save favorite",
                    tint = Color.Red,
                    modifier = modifier
                        .padding(vertical = 8.dp, horizontal = 8.dp)
                )
            }

            IconButton(
                modifier = modifier
                    .align(Alignment.BottomEnd),
                onClick = { context.startActivity(shareIntent, null) }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "save favorite",
                    tint = Color.Black,
                    modifier = modifier
                        .padding(vertical = 8.dp, horizontal = 8.dp)
                )
            }
            Column(
                modifier = modifier
                    .align(Alignment.BottomStart)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Color(0xFFCFE0D5),
                                Color(0xFFabc9e9)
                            )
                        ),
                        shape = RoundedCornerShape(20.dp),
                        alpha = 0.8f
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = article.author ?: "",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic
                    ),

                    )
            }
        }
        Text(
            text = article.title,
            modifier = modifier
                .padding(vertical = 8.dp, horizontal = 8.dp)
        )
    }
}


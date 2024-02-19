package com.example.afnews.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.afnews.R
import com.example.afnews.data.Article
import com.example.afnews.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route: String = "Home"
    override val icon: Int = R.drawable.ic_home_nav_bar
}

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val homeUiState = viewModel.uiState.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val (queryString, onQueryStringChange) = remember { mutableStateOf("") }

    val sortList = listOf("Date", "Popularity")
    val (selectedSortItem, onSortItemChange) = remember {
        mutableStateOf(sortList[0])
    }

    val categoryList = listOf(
        "business",
        "entertainment",
        "general",
        "health",
        "science",
        "sports",
        "technology"
    )
    val (selectedCategory, onCategoryChange) = remember {
        mutableStateOf(sortList[0])
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        SearchPanel(
            queryString = queryString,
            onQueryStringChange = onQueryStringChange,
            onSearchClick = {
//                viewModel.searchNews(queryString)
                onQueryStringChange("")
            }
        )
        SortPanel(
            viewModel = viewModel,
            sortList = sortList,
            selectedItem = selectedSortItem,
            setSelectedItem = {
                onSortItemChange(it)
                onCategoryChange("")
            }
        )
        FilterPanel(
            viewModel = viewModel,
            categoryList = categoryList,
            selectedCategory = selectedCategory,
            onCategoryChange = {
                onCategoryChange(it)
                onSortItemChange("")
            }
        )
        NewsList(homeUiState.value.newsList, loading, viewModel = viewModel)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchPanel(
    queryString: String,
    onQueryStringChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
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
            onClick = {
                onSearchClick()
                coroutineScope.launch {
                    keyboardController?.hide()
                }
            },
            enabled = queryString.isNotEmpty()
        ) {
            Text(text = "Search")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortPanel(
    viewModel: HomeViewModel,
    sortList: List<String>,
    selectedItem: String,
    setSelectedItem: (String) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Sort by: ")
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(sortList) { item ->
                FilterChip(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    selected = (item == selectedItem),
                    onClick = {
                        setSelectedItem(item)
                        if (item == "Popularity") {
                            viewModel.getAllNews(sortBy = "popularity")
                        } else {
                            viewModel.getAllNews()
                        }
                    },
                    label = {
                        Text(text = item)
                    },
                    leadingIcon = {
                        if (item == selectedItem) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(
                                    FilterChipDefaults.IconSize
                                )
                            )
                        } else {
                            null
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPanel(
    viewModel: HomeViewModel,
    categoryList: List<String>,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Category: ")
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(categoryList) { item ->
                FilterChip(
                    modifier = Modifier.padding(horizontal = 6.dp), // gap between items
                    selected = (item == selectedCategory),
                    onClick = {
                        onCategoryChange(item)
                        viewModel.getNewsByCategory(item)
                    },
                    label = {
                        Text(text = item)
                    },
                    leadingIcon = {
                        if (item == selectedCategory) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(
                                    FilterChipDefaults.IconSize
                                )
                            )
                        } else {
                            null
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsList(
    newsList: List<Article>,
    loading: Boolean,
    viewModel: HomeViewModel
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = loading,
        onRefresh = viewModel::getAllNews
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {

        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(newsList) {
                NewsItem(article = it, viewModel = viewModel)
            }
        }

        PullRefreshIndicator(
            refreshing = loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsItem(
    article: Article,
    viewModel: HomeViewModel,
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
                failure = placeholder(R.drawable.news_placeholder),
                modifier = modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Box(
                modifier = modifier
                    .align(Alignment.TopEnd)
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .size(32.dp)
                    .clip(shape = CircleShape)
                    .background(color = Color.White)
                    .clickable { viewModel.saveNews(article) }
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "save favorite",
                    tint = Color.Black,
                    modifier = modifier
                        .align(Alignment.Center)
                        .padding(vertical = 8.dp, horizontal = 8.dp)
                )
            }

            Column(
                modifier = modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Color(0xFFCFE0D5),
                                Color(0xFFabc9e9)
                            )
                        ),
                        shape = RoundedCornerShape(20.dp),
                        alpha = 0.9f
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
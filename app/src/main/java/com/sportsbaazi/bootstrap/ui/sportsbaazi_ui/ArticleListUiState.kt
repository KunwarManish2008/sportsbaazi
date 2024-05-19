package com.sportsbaazi.bootstrap.ui.sportsbaazi_ui

import com.sportsbaazi.bootstrap.data.Result
import com.sportsbaazi.bootstrap.data.response.NewsArticle

data class ArticleListUiState(
    val isLoading: Boolean = true,
    val list: List<NewsArticle>? = emptyList(),
    val error: Result.Error? = null
)

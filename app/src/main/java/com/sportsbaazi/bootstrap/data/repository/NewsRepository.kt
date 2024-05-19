package com.sportsbaazi.bootstrap.data.repository

import com.sportsbaazi.bootstrap.data.response.NewsResponse
import com.sportsbaazi.bootstrap.data.Result
/**
 * Created by Manish Kumar on 18/05/24.
 */
interface NewsRepository {
    suspend fun getArticlesByCategoryAsync(category: String, page: Int): Result<NewsResponse>
}
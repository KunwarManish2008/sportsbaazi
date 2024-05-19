package com.sportsbaazi.bootstrap.ui.sportsbaazi_ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sportsbaazi.bootstrap.data.response.NewsArticle
import com.sportsbaazi.bootstrap.ui.common_composable.HeightSpacer
import com.sportsbaazi.bootstrap.ui.common_composable.RemoteImage
import com.sportsbaazi.bootstrap.ui.common_composable.WidthSpacer
import com.sportsbaazi.bootstrap.ui.common_composable.isLight
import com.sportsbaazi.bootstrap.ui.style.articleTitleStyle
import com.sportsbaazi.bootstrap.ui.style.dateTextStyle
import com.sportsbaazi.bootstrap.ui.style.sourceTextStyle
import com.sportsbaazi.bootstrap.utils.CustomTabUtil

@Composable
fun ArticleRow(article: NewsArticle, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable(onClick = { onClick() })) {
        Row(
            modifier = Modifier.padding(all = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RemoteImage(
                url = article.urlToImage,
                modifier = Modifier.requiredSize(100.dp)
            )
            WidthSpacer(value = 10.dp)
            Column {
                if (!article.source.name.isNullOrEmpty()) {
                    Text(
                        text = article.source.name!!,
                        style = sourceTextStyle.copy(color = MaterialTheme.colorScheme.secondary)
                    )
                    HeightSpacer(value = 4.dp)
                }
                Text(
                    text = article.title,
                    style = articleTitleStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                HeightSpacer(value = 4.dp)
                Text(
                    text = article.publishedAt.substring(0, 10),
                    style = dateTextStyle.copy(color = MaterialTheme.colorScheme.secondary)
                )
            }
        }
        HeightSpacer(value = 10.dp)
        Divider(
            color = MaterialTheme.colorScheme.secondary.copy(
                alpha = 0.2f
            )
        )
    }
}

@Composable
fun ArticleList(articles: List<NewsArticle>) {
    val context = LocalContext.current
    val isDark = MaterialTheme.colorScheme.isLight()
    LazyColumn {
        items(articles) {
            ArticleRow(
                article = it,
                onClick = {
                    CustomTabUtil.launch(context, it.url.toString(), isDark)
                }
            )
        }
    }
}

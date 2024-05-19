package com.sportsbaazi.bootstrap.ui.sportsbaazi_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sportsbaazi.bootstrap.ui.other.Category

/**
 * Created by Akash on 28/08/20
 */

/*@Composable
fun BottomBar(
    categoryList: List<Category>,
    activeCategory: Category,
    onMenuClicked: (Category) -> Unit
) {
    BottomAppBar(
        backgroundColor = MaterialTheme.colorScheme.surface,
        elevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(50.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            categoryList.forEach { category ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = category.icon),
                            contentDescription = category.category,
                        )
                    },
                    selected = activeCategory == category,
                    onClick = {
                        onMenuClicked(category)
                    },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}*/

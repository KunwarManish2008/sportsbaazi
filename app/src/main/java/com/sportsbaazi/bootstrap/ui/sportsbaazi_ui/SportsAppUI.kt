package com.sportsbaazi.bootstrap.ui.sportsbaazi_ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.sportsbaazi.bootstrap.R
import com.sportsbaazi.bootstrap.models.Data
import com.sportsbaazi.bootstrap.models.Players
import com.sportsbaazi.bootstrap.ui.other.Category
import com.sportsbaazi.bootstrap.ui.other.getTitleResource
import com.sportsbaazi.bootstrap.ui.style.dateTextStyle
import com.sportsbaazi.bootstrap.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsAppUI(viewModel: NewsViewModel) {
    val activeCategory = viewModel.activeCategory.observeAsState().value!!
    val uiState = viewModel.activeCategoryUiState.observeAsState().value!!

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "SportsBaazi", fontWeight = FontWeight(600))
                },
                /*navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },*/
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                ),
            )
        },
        content = {it
            BodyContent(
                viewModel = viewModel,
                innerPadding = it,
                activeCategory = activeCategory,
                activeCategoryUiState = uiState,
                onThemeSwitch = {
                    viewModel.performAction(NewsViewModel.Action.SwitchTheme)
                },
                retryFetchingArticles = { category ->
                    viewModel.performAction(NewsViewModel.Action.FetchArticles(category))
                }
            )
        },
    )
}

@Composable
fun BodyContent(
    viewModel: NewsViewModel,
    innerPadding: PaddingValues,
    activeCategory: Category,
    activeCategoryUiState: ArticleListUiState,
    onThemeSwitch: () -> Unit,
    retryFetchingArticles: (Category) -> Unit
) {
    val stringRes = getTitleResource(activeCategory)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            /*TopAppBar(stringRes, onThemeSwitch = {
                onThemeSwitch()
            })
            NewzzListContainer(
                uiState = activeCategoryUiState,
                retry = {
                    retryFetchingArticles(activeCategory)
                }
            )*/
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.TopCenter
            ) {
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .padding(innerPadding),
                contentAlignment = Alignment.TopCenter
            ) {
                PromotionCard(viewModel)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            ){
                HorizontalPagerWithIndicatorsScreen(viewModel)
            }
        }

    }
}

@Composable
fun HorizontalPagerWithIndicatorsScreen(viewModel: NewsViewModel) {
    val playersList = viewModel.playersList
    Column {
        HorizontalPagerWithIndicators(playersList)
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerWithIndicators(players: List<Data>) {
    val pagerState = rememberPagerState(pageCount = {
        players.size
    })
    val itemSpacing = 2.dp
    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(0)
            ),
            contentPadding = PaddingValues(horizontal = 24.dp),
            pageSpacing = itemSpacing
            ) {
            ProfileCard(players[it])
        }

        /*HorizontalPagerIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(10.dp),
            pageCount = players.size,
            pagerState = pagerState,
        )*/
    }


}

@Composable
private fun PromotionCard(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier
            .weight(1f).padding(18.dp).fillMaxSize()
        ){
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.sportsbaazi_banner2),
                contentDescription = ""
            )
        }
        Button(onClick = {
            viewModel.downloadApkFile()
        }, modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(vertical = 8.dp)) {
            Text(text = "Download Now")
        }
    }
}

@Composable
private fun ProfileCard(
    currentPlayer: Data,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                painter = painterResource(R.drawable.android),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = currentPlayer.fullName)

                Text(text = "Cool subtitle text", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun ProfileItem(
    idx: Int,
    modifier: Modifier = Modifier,
    onItemClick: (idx: Int) -> Unit, // Also, lambda for pass item clicks outside of current composable
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.Black)
            .clickable { onItemClick(idx) },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = "Item on index: $idx", color = Color.White)
    }
}

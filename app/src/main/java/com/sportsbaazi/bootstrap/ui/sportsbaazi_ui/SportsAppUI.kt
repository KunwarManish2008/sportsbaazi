package com.sportsbaazi.bootstrap.ui.sportsbaazi_ui

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.sportsbaazi.bootstrap.R
import com.sportsbaazi.bootstrap.models.Blog
import com.sportsbaazi.bootstrap.ui.other.Category
import com.sportsbaazi.bootstrap.ui.other.getTitleResource
import com.sportsbaazi.bootstrap.ui.theme.dimens
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
                    Image(
                        painter = painterResource(id = R.drawable.sportsbaazi_corner),
                        contentDescription = "",
                        modifier = Modifier.padding(MaterialTheme.dimens.logoSize))
//                    Text(text = "SportsBaazi", fontWeight = FontWeight(800), fontFamily = FontFamily.SansSerif)
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
                    .padding(MaterialTheme.dimens.small1),
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
                    .padding(MaterialTheme.dimens.small1)
            ){
                HorizontalPagerWithIndicatorsScreen(viewModel)
            }
        }

    }
}

@Composable
fun HorizontalPagerWithIndicatorsScreen(viewModel: NewsViewModel) {
    val blogList = viewModel.blogList
    Column {
        HorizontalPagerWithIndicators(blogList)
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerWithIndicators(blogs: List<Blog>) {
    val pagerState = rememberPagerState(pageCount = {
        blogs.size
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
            contentPadding = PaddingValues(horizontal = MaterialTheme.dimens.small3),
            pageSpacing = itemSpacing
            ) {
            BlogCard(blogs[it])
        }

        HorizontalPagerIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(MaterialTheme.dimens.small1),
            pageCount = 5,
            pagerState = pagerState,
        )
    }


}

@Composable
private fun PromotionCard(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()

            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.setSupportZoom(true)
        }
    }
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.dimens.small1),
        elevation = CardDefaults.cardElevation(defaultElevation = 2. dp),
        onClick = {
            println("clicked")
            webView.loadUrl("https://link.sportsbaazi.com/G2OuU1JYQJb")
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
        ){
            Image(
                modifier = Modifier.fillMaxSize()
                    .clip(RectangleShape),
                painter = painterResource(id = R.drawable.sports_content),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
        }

        /*Button(onClick = {
            viewModel.downloadApkFile()
        }, modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(vertical = MaterialTheme.dimens.small1)) {
            Text(text = "Download Now", fontFamily = FontFamily.SansSerif)
        }*/
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BlogCard(
    blog: Blog,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.dimens.small1),
        shape = RoundedCornerShape(MaterialTheme.dimens.small1),
        onClick = {},
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.dimens.small1),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .height(MaterialTheme.dimens.large + MaterialTheme.dimens.medium3 * 2 + MaterialTheme.dimens.medium2)
                    .fillMaxWidth()
                    .clip(RectangleShape),
                painter = painterResource(blog.blogImage),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Divider(
                Modifier
                    .height(2.dp)
                    .fillMaxWidth())
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
            ) {
                ExpandableText(
                    text = blog.blogContent,
                    textAlign = TextAlign.Justify
                )
//                Text(text = "Cool subtitle text", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

const val DEFAULT_MINIMUM_TEXT_LINE = 10

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = LocalTextStyle.current,
    fontStyle: FontStyle? = null,
    text: String,
    collapsedMaxLine: Int = MaterialTheme.dimens.small1.value.toInt(),
    showMoreText: String = "... Show More",
    showMoreStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.W500),
    showLessText: String = " Show Less",
    showLessStyle: SpanStyle = showMoreStyle,
    textAlign: TextAlign? = null
) {
    var isExpanded by remember { mutableStateOf(false) }
    var clickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableStateOf(0) }
    Box(modifier = Modifier
        .clickable(clickable) {
            isExpanded = !isExpanded
        }
        .then(modifier)
    ) {
        Text(
            modifier = textModifier
                .fillMaxWidth()
                .animateContentSize(),
            text = buildAnnotatedString {
                if (clickable) {
                    if (isExpanded) {
                        append(text)
                        withStyle(style = showLessStyle) { append(showLessText + "\n") }
                    } else {
                        val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                            .dropLast(showMoreText.length)
                            .dropLastWhile { Character.isWhitespace(it) || it == '.' }
                        append(adjustText)
                        withStyle(style = showMoreStyle) { append(showMoreText + "\n") }
                    }
                } else {
                    append(text)
                }
            },

            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
            fontStyle = fontStyle,
            onTextLayout = { textLayoutResult ->
                if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                    clickable = true
                    lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLine - 1)
                }
            },
            style = style,
            textAlign = textAlign
        )
    }


}

@Composable
fun WebViewScreen() {

    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()

            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.setSupportZoom(true)
            loadUrl("https://link.sportsbaazi.com/G2OuU1JYQJb")
        }
    }
}
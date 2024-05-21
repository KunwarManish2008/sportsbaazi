package com.sportsbaazi.bootstrap

import android.app.Application
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Observer
import com.sportsbaazi.bootstrap.downloadmanager.AndroidDownloader
import com.sportsbaazi.bootstrap.ui.sportsbaazi_ui.NewsAppUI
import com.sportsbaazi.bootstrap.ui.theme.SportsBaaziTheme
import com.sportsbaazi.bootstrap.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkTheme = viewModel.isDarkTheme.observeAsState(false)
            SportsBaaziTheme(darkTheme = darkTheme.value) {
                NewsAppUI(viewModel = viewModel)
            }
        }

        val fileInString: String =
            applicationContext.assets.open("players.json").bufferedReader().use { it.readText() }

        viewModel.customInit(fileInString)
        viewModel.downloadApk.observe(this, Observer {
            if(it) {
                val downloader = AndroidDownloader(context = this)
                downloader.downloadFile("https://link.sportsbaazi.com/G2OuU1JYQJb")
            }
        })
        /*setContent {
            SportsBaaziTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }*/
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SportsBaaziTheme {
        Greeting("Android")
    }
}
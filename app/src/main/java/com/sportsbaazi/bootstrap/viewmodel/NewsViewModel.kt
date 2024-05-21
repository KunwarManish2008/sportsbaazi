package com.sportsbaazi.bootstrap.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sportsbaazi.bootstrap.data.Result
import com.sportsbaazi.bootstrap.data.repository.NewsRepository
import com.sportsbaazi.bootstrap.data.response.NewsResponse
import com.sportsbaazi.bootstrap.downloadmanager.AndroidDownloader
import com.sportsbaazi.bootstrap.models.Blog
import com.sportsbaazi.bootstrap.models.Data
import com.sportsbaazi.bootstrap.models.Players
import com.sportsbaazi.bootstrap.staticdata.canShivamDubeyContent
import com.sportsbaazi.bootstrap.staticdata.canShivamDubeyContentSummary
import com.sportsbaazi.bootstrap.staticdata.canShivamDubeyImage
import com.sportsbaazi.bootstrap.staticdata.fiveTeamContentSummary
import com.sportsbaazi.bootstrap.staticdata.fiveTeamsContent
import com.sportsbaazi.bootstrap.staticdata.fiveTeamsImage
import com.sportsbaazi.bootstrap.staticdata.indiaWinContent
import com.sportsbaazi.bootstrap.staticdata.indiaWinContentSummary
import com.sportsbaazi.bootstrap.staticdata.indiaWinImage
import com.sportsbaazi.bootstrap.staticdata.recordsMadeContent
import com.sportsbaazi.bootstrap.staticdata.recordsMadeContentSummary
import com.sportsbaazi.bootstrap.staticdata.recordsMadeImage
import com.sportsbaazi.bootstrap.staticdata.tenBestRecordContent
import com.sportsbaazi.bootstrap.staticdata.tenBestRecordContentSummary
import com.sportsbaazi.bootstrap.staticdata.tenBestRecordPoster
import com.sportsbaazi.bootstrap.ui.sportsbaazi_ui.ArticleListUiState
import com.sportsbaazi.bootstrap.ui.other.Business
import com.sportsbaazi.bootstrap.ui.other.Category
import com.sportsbaazi.bootstrap.ui.other.General
import com.sportsbaazi.bootstrap.ui.other.Technology
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(private val repo: NewsRepository) : ViewModel() {

    private val _isDarkTheme = MutableLiveData<Boolean>().apply { value = false }
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    private val _downloadApk = MutableLiveData<Boolean>().apply { value = false }
    val downloadApk: LiveData<Boolean> = _downloadApk

    private val _categoryList = MutableLiveData<List<Category>>().apply {
        value = listOf(General(), Business(), Technology())
    }
    val categoryList: LiveData<List<Category>> = _categoryList

    private val _activeCategory = MutableLiveData<Category>().apply {
        value = categoryList.value!![1]
    }
    val activeCategory: LiveData<Category> = _activeCategory

    private val _activeCategoryUiState = MutableLiveData<ArticleListUiState>().apply {
        value = ArticleListUiState()
    }
    val activeCategoryUiState: LiveData<ArticleListUiState> = _activeCategoryUiState

    lateinit var playersList: List<Data>
    lateinit var blogList: List<Blog>
    init {
        getArticlesByCategory(categoryList.value!![0])

    }

    fun customInit(fileInString: String) {
        setPlayersData(fileInString)
        prepareBlogData()
    }

    private fun prepareBlogData() {
        blogList = listOf(
            Blog(blogContent = fiveTeamsContent, blogImage = fiveTeamsImage, blogContentSummary = fiveTeamContentSummary),
            Blog(blogContent = canShivamDubeyContent, blogImage = canShivamDubeyImage, blogContentSummary = canShivamDubeyContentSummary),
            Blog(blogContent = indiaWinContent, blogImage = indiaWinImage, blogContentSummary = indiaWinContentSummary),
            Blog(blogContent = tenBestRecordContent, blogImage = tenBestRecordPoster, blogContentSummary = tenBestRecordContentSummary),
            Blog(blogContent = recordsMadeContent, blogImage = recordsMadeImage, blogContentSummary = recordsMadeContentSummary)
        )
    }
    private fun getArticlesByCategory(
        category: Category,
        page: Int = 1
    ) {
        viewModelScope.launch {
            val activeState = ArticleListUiState()
            setLoadingState(activeState)
            when (val result = repo.getArticlesByCategoryAsync(category.category, page)) {
                is Result.Error -> {
                    withContext(Dispatchers.Main) {
                        setErrorState(
                            activeState,
                            Result.Error(result.errorMessage, result.showRetry)
                        )
                    }
                }
                is Result.Success -> {
                    withContext(Dispatchers.Main) {
                        setSuccessState(activeState, result.data)
                    }
                }
            }
        }
    }

    fun downloadApkFile() {
        _downloadApk.value = true
    }

    private fun setPlayersData(fileInString: String) {
        val gson = Gson()
        val players : Players = gson.fromJson(fileInString, Players::class.java)
        playersList = players.data
    }

    fun performAction(action: Action) {
        when (action) {
            is Action.ChangePageTo -> {
                _activeCategory.value = action.category
                getArticlesByCategory(_activeCategory.value!!)
            }
            is Action.FetchArticles -> {
                getArticlesByCategory(action.category)
            }
            Action.SwitchTheme -> {
                _isDarkTheme.value = !_isDarkTheme.value!!
            }

            else -> {}
        }
    }

    private fun setErrorState(activeState: ArticleListUiState, error: Result.Error) {
        _activeCategoryUiState.value = activeState.copy(
            isLoading = false,
            list = null,
            error = error
        )
    }

    private fun setSuccessState(activeState: ArticleListUiState, data: NewsResponse) {
        _activeCategoryUiState.value = activeState.copy(
            isLoading = false,
            list = data.articles,
            error = null
        )
    }

    private fun setLoadingState(activeState: ArticleListUiState) {
        _activeCategoryUiState.value = activeState.copy(isLoading = true)
    }

    sealed class Action {
        data class ChangePageTo(val category: Category) : Action()
        data class FetchArticles(val category: Category) : Action()
        object SwitchTheme : Action()
    }
}

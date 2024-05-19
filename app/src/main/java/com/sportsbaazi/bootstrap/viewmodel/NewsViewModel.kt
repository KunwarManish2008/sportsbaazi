package com.sportsbaazi.bootstrap.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsbaazi.bootstrap.data.Result
import com.sportsbaazi.bootstrap.data.repository.NewsRepository
import com.sportsbaazi.bootstrap.data.response.NewsResponse
import com.sportsbaazi.bootstrap.downloadmanager.AndroidDownloader
import com.sportsbaazi.bootstrap.ui.sportsbaazi_ui.ArticleListUiState
import com.sportsbaazi.bootstrap.ui.other.Business
import com.sportsbaazi.bootstrap.ui.other.Category
import com.sportsbaazi.bootstrap.ui.other.General
import com.sportsbaazi.bootstrap.ui.other.Technology
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    init {
        getArticlesByCategory(categoryList.value!![0])
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

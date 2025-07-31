package com.example.mysamplerepoapp.ui.list.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.mysamplerepoapp.data.model.RepoResponse
import com.example.mysamplerepoapp.data.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val repository: GitHubRepository
) : ViewModel() {

    val repos = repository.getRepos()
        .flow
        .cachedIn(viewModelScope)

    val bookmarkedIds = repository.getBookmarkedIds().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun toggleBookmark(repo: RepoResponse, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(repo, isBookmarked)
        }
    }

}

package com.example.mysamplerepoapp.ui.details.activity

import androidx.lifecycle.ViewModel
import com.example.mysamplerepoapp.data.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepoDetailsViewModel @Inject constructor(
    private val repoRepository: GitHubRepository
) : ViewModel() {

}

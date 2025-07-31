package com.example.mysamplerepoapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.mysamplerepoapp.data.api.GitHubApiService
import com.example.mysamplerepoapp.data.db.RepoDao
import com.example.mysamplerepoapp.data.model.Repo
import com.example.mysamplerepoapp.data.model.RepoResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubRepository @Inject constructor(
    private val api: GitHubApiService,
    private val dao: RepoDao
) {
    fun getRepos(): Pager<Int, RepoResponse> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { ReposPagingSource(api) }
        )
    }


    fun getBookmarkedIds(): Flow<List<Long>> = dao.getBookmarkedIds()

    suspend fun toggleBookmark(repo: RepoResponse, isBookmarked: Boolean) {
        if (isBookmarked) {
            dao.delete(Repo(repo.id, repo.full_name, repo.stargazers_count))
        } else {
            dao.insert(Repo(repo.id, repo.full_name, repo.stargazers_count))
        }
    }
}
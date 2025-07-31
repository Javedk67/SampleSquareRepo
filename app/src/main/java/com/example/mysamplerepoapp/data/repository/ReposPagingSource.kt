package com.example.mysamplerepoapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mysamplerepoapp.data.api.GitHubApiService
import com.example.mysamplerepoapp.data.model.RepoResponse
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ReposPagingSource @Inject constructor(
    private val api: GitHubApiService
) : PagingSource<Int, RepoResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoResponse> {
        return try {
            val page = params.key ?: 1
            val response = api.getRepos(page, params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RepoResponse>): Int? {
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }
    }
}
package com.example.mysamplerepoapp.data.api


import com.example.mysamplerepoapp.data.model.RepoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApiService {
    @GET("orgs/square/repos")
    suspend fun getRepos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): List<RepoResponse>
}


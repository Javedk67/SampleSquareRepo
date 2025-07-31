package com.example.mysamplerepoapp.di

import android.content.Context
import androidx.room.Room
import com.example.mysamplerepoapp.data.api.GitHubApiService
import com.example.mysamplerepoapp.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApi(r: Retrofit): GitHubApiService =
        r.create(GitHubApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "repos_db").build()

    @Provides
    fun provideRepoDao(db: AppDatabase) = db.repoDao()
}

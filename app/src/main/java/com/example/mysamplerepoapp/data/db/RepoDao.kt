package com.example.mysamplerepoapp.data.db

import androidx.room.*
import com.example.mysamplerepoapp.data.model.Repo
import kotlinx.coroutines.flow.Flow


@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmark: Repo)

    @Delete
    suspend fun delete(bookmark: Repo)

    @Query("SELECT id FROM bookmarks")
    fun getBookmarkedIds(): Flow<List<Long>>
}

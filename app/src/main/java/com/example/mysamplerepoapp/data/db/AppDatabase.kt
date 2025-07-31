package com.example.mysamplerepoapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mysamplerepoapp.data.model.Repo

@Database(entities = [Repo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}

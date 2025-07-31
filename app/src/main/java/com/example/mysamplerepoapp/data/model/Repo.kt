package com.example.mysamplerepoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "bookmarks")
data class Repo(
    @PrimaryKey val id: Long,
    val name: String,
    val stars: Int
)


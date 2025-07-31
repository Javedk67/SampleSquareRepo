package com.example.mysamplerepoapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RepoResponse(
    val id: Long,
    val full_name: String,
    val stargazers_count: Int,
    var isBookmarked: Boolean = false
):Parcelable

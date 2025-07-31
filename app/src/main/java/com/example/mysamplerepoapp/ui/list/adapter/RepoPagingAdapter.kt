package com.example.mysamplerepoapp.ui.list.adapter

import android.annotation.SuppressLint
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mysamplerepoapp.data.model.RepoResponse
import com.example.mysamplerepoapp.databinding.ItemRepoBinding

class RepoPagingAdapter(private val onClick: (RepoResponse, Boolean) -> Unit) :
    PagingDataAdapter<RepoResponse, RepoPagingAdapter.RepoPagingViewHolder>(
        diffCallback
    ) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<RepoResponse>() {
            override fun areItemsTheSame(old: RepoResponse, new: RepoResponse) = old.id == new.id
            override fun areContentsTheSame(old: RepoResponse, new: RepoResponse) = old == new
        }
    }

    private var lastClickTime = 0L
    private var bookmarkedIds = emptyList<Long>()

    fun setBookmarkedIds(ids: List<Long>) {
        bookmarkedIds = ids
        notifyDataSetChanged()
    }

    inner class RepoPagingViewHolder(private val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(repo: RepoResponse) {
            binding.repoName.text = repo.full_name
            binding.repoStars.text = "⭐ ${repo.stargazers_count}"
            val isBookmarked = bookmarkedIds.contains(repo.id)
            if (isBookmarked) {
                binding.bookMarkImg.visibility = View.VISIBLE
            } else {
                binding.bookMarkImg.visibility = View.GONE
            }
            itemView.setOnClickListener {
                val currentTime = SystemClock.elapsedRealtime()
                if (currentTime - lastClickTime > 500) { // 500ms debounce
                    lastClickTime = currentTime
                    onClick.invoke(repo, isBookmarked)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoPagingViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoPagingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoPagingViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}
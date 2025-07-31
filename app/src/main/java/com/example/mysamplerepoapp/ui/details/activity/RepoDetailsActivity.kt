package com.example.mysamplerepoapp.ui.details.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.mysamplerepoapp.R
import com.example.mysamplerepoapp.data.model.RepoResponse
import com.example.mysamplerepoapp.databinding.ActivityRepoDetailsBinding
import com.example.mysamplerepoapp.ui.list.activity.RepoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepoDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepoDetailsBinding

    private val vm: RepoListViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.repo_detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbarDetail.title = getString(R.string.detail)
        val repoData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("repo", RepoResponse::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<RepoResponse>("repo")
        }

        val isBookmarked = intent.getBooleanExtra("repo_bookmarked", false)

        val repo = RepoResponse(
            repoData!!.id,
            repoData.full_name,
            repoData.stargazers_count,
            isBookmarked
        )
        binding.repoName.text = repo.full_name
        binding.repoStars.text = "⭐ ${repo.stargazers_count}"
        updateButton(repo.isBookmarked)

        binding.bookmarkButton.setOnClickListener {
            //viewModel.toggleBookmark(repo)
            repo.isBookmarked = true
            updateButton(repo.isBookmarked)
        }

        lifecycleScope.launch {
            vm.bookmarkedIds.collect { ids ->
                val isBookmarked = ids.contains(repo.id)
                binding.bookmarkButton.text =
                    if (isBookmarked) "Remove Bookmark" else "Add Bookmark"
                binding.bookmarkButton.setOnClickListener {
                    vm.toggleBookmark(
                        RepoResponse(repo.id, repo.full_name, repo.stargazers_count),
                        isBookmarked
                    )
                }
            }
        }
    }

    private fun updateButton(isBookmarked: Boolean) {
        binding.bookmarkButton.text =
            if (isBookmarked) "Remove Bookmark" else "Add Bookmark"
    }
}

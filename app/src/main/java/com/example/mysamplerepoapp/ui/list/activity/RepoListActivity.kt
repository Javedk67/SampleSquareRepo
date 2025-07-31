package com.example.mysamplerepoapp.ui.list.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysamplerepoapp.R
import com.example.mysamplerepoapp.data.model.RepoResponse
import com.example.mysamplerepoapp.databinding.ActivityRepoListBinding
import com.example.mysamplerepoapp.ui.details.activity.RepoDetailsActivity
import com.example.mysamplerepoapp.ui.list.adapter.RepoLoadStateAdapter
import com.example.mysamplerepoapp.ui.list.adapter.RepoPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepoListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepoListBinding
    private val viewModel: RepoListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.repo_list)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.toolbar.title = getString(R.string.repository_list)


        val adapter = RepoPagingAdapter(onClick = { repo, isMarked ->
            //viewModel.toggleBookmark(repo, isMarked)
            openDetails(repo, isMarked)
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter =
            adapter.withLoadStateFooter(footer = RepoLoadStateAdapter { adapter.retry() })

        lifecycleScope.launch {
            viewModel.repos.collectLatest {
                adapter.submitData(it)

            }
        }

        lifecycleScope.launch {
            viewModel.bookmarkedIds.collect { ids -> adapter.setBookmarkedIds(ids) }
        }

        adapter.addLoadStateListener { loadState ->
            // Show loader only when refreshing for the first time
            binding.progressBar.visibility =
                if (loadState.refresh is LoadState.Loading) View.VISIBLE else View.GONE

            // Handle error state
            val errorState = loadState.source.refresh as? LoadState.Error
                ?: loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error

            errorState?.let {
                Toast.makeText(this, "Error: ${it.error.localizedMessage}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun openDetails(repo: RepoResponse, marked: Boolean) {
        val bundle = Bundle().apply {
            putParcelable("repo", repo) // putParcelable
            putBoolean("repo_bookmarked", marked) // optional extra flag
        }
        val intent = Intent(this, RepoDetailsActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}

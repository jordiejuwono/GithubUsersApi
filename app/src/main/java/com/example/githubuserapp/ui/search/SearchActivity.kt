package com.example.githubuserapp.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.databinding.ActivitySearchBinding
import com.example.githubuserapp.model.users.GithubUsersItem
import com.example.githubuserapp.ui.detail.UserDetailActivity
import com.example.githubuserapp.ui.main.adapter.GithubUserAdapter
import com.example.githubuserapp.viewmodel.githubusers.GithubViewModel
import com.example.githubuserapp.viewmodel.githubusers.ViewModelFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: GithubUserAdapter
    private lateinit var viewModel: GithubViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = query
        setViewBinding()
        viewModel = obtainViewModel(this)
        getData()
        setRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): GithubViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[GithubViewModel::class.java]
    }

    private fun getData() {
        viewModel.getAllSearchedUsers(this, query)
    }

    private fun setData() {
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.noResults.observe(this) {
            showNoResults(it)
        }
        viewModel.searchGithubUsers.observe(this) { response ->
            response.items?.let {
                adapter.setItems(it)
            }
        }
    }

    private fun setViewBinding(){
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setRecyclerView() {
        adapter = GithubUserAdapter(object : GithubUserAdapter.IntentToProfileDetails {
            override fun intentToDetails(githubUsersItem: GithubUsersItem) {
                val intent = Intent(this@SearchActivity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.USER_DETAILS, githubUsersItem)
                startActivity(intent)
            }

        })
        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvSearchGithubUsers.apply {
            adapter = this@SearchActivity.adapter
            this.layoutManager = layoutManager
            addItemDecoration(itemDecoration)
        }

        //set data to adapter
        setData()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoading.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    private fun showNoResults(noResults: Boolean) {
        binding.tvNoResults.visibility = if(noResults) View.VISIBLE else View.GONE
    }

    companion object {
        var query = ""
    }
}
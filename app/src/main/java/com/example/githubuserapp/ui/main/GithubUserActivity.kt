package com.example.githubuserapp.ui.main

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.databinding.ActivityGithubUserBinding
import com.example.githubuserapp.model.users.GithubUsersItem
import com.example.githubuserapp.ui.detail.UserDetailActivity
import com.example.githubuserapp.ui.main.adapter.GithubUserAdapter
import com.example.githubuserapp.viewmodel.GithubViewModel

class GithubUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGithubUserBinding
    private lateinit var adapter: GithubUserAdapter
    private val viewModel by viewModels<GithubViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.text_github_users)
        setViewBinding()
        getData()
        setRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_top, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.query_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getAllSearchedUsers(this@GithubUserActivity, query.toString())
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query?.isEmpty() == true) {
                    getData()
                }
                return true
            }
        })
        return true
    }

    private fun getData() {
        viewModel.getAllGithubUsers(this)
    }

    private fun setData() {
        viewModel.githubUsersList.observe(this) {
            adapter.setItems(it)
        }
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
        binding = ActivityGithubUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setRecyclerView() {
        adapter = GithubUserAdapter(object : GithubUserAdapter.IntentToProfileDetails {
            override fun intentToDetails(githubUsersItem: GithubUsersItem) {
                val intent = Intent(this@GithubUserActivity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.USER_DETAILS, githubUsersItem)
                startActivity(intent)
            }

        })
        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvGithubUsers.apply {
            adapter = this@GithubUserActivity.adapter
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
}
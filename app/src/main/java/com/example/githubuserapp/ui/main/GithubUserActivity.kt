package com.example.githubuserapp.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserapp.R
import com.example.githubuserapp.data.datastore.SettingPreferences
import com.example.githubuserapp.databinding.ActivityGithubUserBinding
import com.example.githubuserapp.ui.favorites.FavoritesFragment
import com.example.githubuserapp.ui.search.SearchActivity
import com.example.githubuserapp.ui.settings.SettingsFragment
import com.example.githubuserapp.viewmodel.datastore.DataStoreViewModel
import com.example.githubuserapp.viewmodel.datastore.DataStoreViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class GithubUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGithubUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getThemeSettings()
        supportActionBar?.title = getString(R.string.text_github_users)
        setViewBinding()
        setFragment(GithubUsersFragment())
        bottomNavListeners()
    }

    private fun setViewBinding(){
        binding = ActivityGithubUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fl_container, fragment)
        }
    }

    private fun bottomNavListeners() {
        binding.bnvUsers.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    setFragment(GithubUsersFragment())
                    supportActionBar?.show()
                }
                R.id.action_favorites -> {
                    setFragment(FavoritesFragment())
                    supportActionBar?.hide()
                }
                R.id.action_settings -> {
                    setFragment(SettingsFragment())
                    supportActionBar?.hide()
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_top, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.query_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    SearchActivity.query = query.toString()
                    val intent = Intent(this@GithubUserActivity, SearchActivity::class.java)
                    startActivity(intent)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })
        return true
    }

    private fun getThemeSettings() {
        val pref = SettingPreferences.getInstance(this.dataStore)
        val viewModelDataStore = ViewModelProvider(this, DataStoreViewModelFactory(pref))[DataStoreViewModel::class.java]

        viewModelDataStore.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

}
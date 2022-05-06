package com.example.githubuserapp.ui.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.data.room.UsersEntity
import com.example.githubuserapp.databinding.ActivityUserDetailsBinding
import com.example.githubuserapp.model.users.GithubUsersItem
import com.example.githubuserapp.ui.detail.adapter.ViewPagerAdapter
import com.example.githubuserapp.ui.detail.detailfragment.FollowersFragment
import com.example.githubuserapp.ui.detail.detailfragment.FollowingFragment
import com.example.githubuserapp.viewmodel.githubusers.GithubViewModel
import com.example.githubuserapp.viewmodel.githubusers.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    companion object {
        const val USER_DETAILS = "USER_DETAILS"
        const val USER_DETAILS_FAVORITE = "USER_DETAILS_FAVORITE"

        const val GET_DATA_FROM = "GET_DATA_FROM"
        const val FROM_HOME = 0
        const val FROM_FAVORITES = 1

        @JvmStatic
        fun startActivityFavorites(context: Context?, detailsMode: Int, data: UsersEntity) {
            val intent = Intent(context, UserDetailActivity::class.java)
            intent.putExtra(GET_DATA_FROM, detailsMode)
            intent.putExtra(USER_DETAILS_FAVORITE, data)
            context?.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var viewModel: GithubViewModel
    private var detailsMode = FROM_HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentMode()
        viewModel = obtainViewModel(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setViewBinding()
        setViewPager()
        getDataFromIntent()
    }

    private fun getIntentMode() {
        detailsMode = intent.getIntExtra(GET_DATA_FROM, FROM_HOME)
    }

    private fun obtainViewModel(activity: AppCompatActivity): GithubViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[GithubViewModel::class.java]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.action_add_favorites -> {
                MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                    .setTitle("Add to Favorites")
                    .setMessage("Do you want to add this user to favorite?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        val userDetails = intent.getParcelableExtra<GithubUsersItem>(USER_DETAILS)
                        val userLogin = userDetails?.login
                        val userName = userDetails?.login
                        val userPhoto = userDetails?.avatarUrl
                        viewModel.insert(
                            UsersEntity(
                                userLogin.toString(),
                                userName.toString(),
                                userPhoto.toString()
                            )
                        )
                        Toast.makeText(this, "User Added to Favorites!", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
            R.id.action_remove -> {
                MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                    .setTitle("Add to Favorites")
                    .setMessage("Do you want to remove this user from favorite?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        val userDataFromFavorites =
                            intent.getParcelableExtra<UsersEntity>(USER_DETAILS_FAVORITE)

                        viewModel.delete(userDataFromFavorites!!)
                        Toast.makeText(this, "Removed from Favorites!", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        finish()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (detailsMode == FROM_FAVORITES) {
            menuInflater.inflate(R.menu.menu_details_favorites, menu)
        } else {
            menuInflater.inflate(R.menu.menu_top_details, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setViewBinding() {
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getDataFromIntent() {
        val userData = intent.getParcelableExtra<GithubUsersItem>(USER_DETAILS)
        val userDataFromFavorites = intent.getParcelableExtra<UsersEntity>(USER_DETAILS_FAVORITE)
        val userId = if (detailsMode == FROM_FAVORITES) {
            userDataFromFavorites?.login
        } else {
            userData?.login
        }

        //set data to followers and following fragments
        FollowersFragment.userId = userId.toString()
        FollowingFragment.userId = userId.toString()

        viewModel.getUserDetails(this, userId.toString())

        viewModel.userDetails.observe(this) {
            supportActionBar?.title = it.name
            if (it.name == null) {
                binding.tvName.text = getString(R.string.text_empty_api)
            } else {
                binding.tvName.text = it.name
            }
            binding.tvName.text = it.name
            binding.tvUsername.text = it.login
            binding.ivAvatar.loadImage(it.avatarUrl)
            if (it.location == null) {
                binding.tvLocation.text = getString(R.string.text_empty_api)
            } else {
                binding.tvLocation.text = it.location
            }
            if (it.company == null) {
                binding.tvWorkplace.text = getString(R.string.text_empty_api)
            } else {
                binding.tvWorkplace.text = it.company
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .into(this)
    }

    private fun setViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        val viewPager = findViewById<ViewPager2>(R.id.vp_following)
        viewPager.adapter = viewPagerAdapter
        val tabLayout = findViewById<TabLayout>(R.id.tl_following)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.text_followers)
                1 -> tab.text = getString(R.string.text_following)
            }
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
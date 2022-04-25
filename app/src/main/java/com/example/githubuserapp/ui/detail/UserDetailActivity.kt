package com.example.githubuserapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.databinding.ActivityUserDetailsBinding
import com.example.githubuserapp.model.users.GithubUsersItem
import com.example.githubuserapp.ui.detail.adapter.ViewPagerAdapter
import com.example.githubuserapp.ui.detail.detailfragment.FollowersFragment
import com.example.githubuserapp.ui.detail.detailfragment.FollowingFragment
import com.example.githubuserapp.viewmodel.GithubViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    companion object {
        const val USER_DETAILS = "USER_DETAILS"
    }

    private lateinit var binding: ActivityUserDetailsBinding
    private val viewModel by viewModels<GithubViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setViewBinding()
        setViewPager()
        getDataFromIntent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setViewBinding() {
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getDataFromIntent() {
        val userData = intent.getParcelableExtra<GithubUsersItem>(USER_DETAILS)
        val userId = userData?.login

        //set data to followers and following fragments
        FollowersFragment.userId = userId.toString()
        FollowingFragment.userId = userId.toString()

        viewModel.getUserDetails(this, userId.toString())

        viewModel.userDetails.observe(this) {
            supportActionBar?.title = it.name
            binding.tvName.text = it.name
            binding.tvUsername.text = it.login
            binding.ivAvatar.loadImage(it.avatarUrl)
            if (it.location == null) {
                binding.tvLocation.text = "-"
            } else {
                binding.tvLocation.text = it.location
            }
            if (it.company == null) {
                binding.tvWorkplace.text = "-"
            } else {
                binding.tvWorkplace.text = it.company
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    fun ImageView.loadImage(url: String?) {
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
package com.example.githubuserapp.ui.detail.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuserapp.ui.detail.detailfragment.FollowersFragment
import com.example.githubuserapp.ui.detail.detailfragment.FollowingFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return ITEM_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FollowersFragment()
            1 -> FollowingFragment()
            else -> Fragment()
        }
    }

    companion object {
        const val ITEM_COUNT = 2
    }
}
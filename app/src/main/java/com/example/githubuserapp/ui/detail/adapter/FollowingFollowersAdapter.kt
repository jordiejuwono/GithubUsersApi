package com.example.githubuserapp.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.databinding.FollowingFollowersItemBinding
import com.example.githubuserapp.model.users.GithubUsersItem

class FollowingFollowersAdapter :
    RecyclerView.Adapter<FollowingFollowersAdapter.FollowingFollowersViewHolder>() {

    val items: ArrayList<GithubUsersItem> = arrayListOf()

    class FollowingFollowersViewHolder(val binding: FollowingFollowersItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowingFollowersViewHolder {
        return FollowingFollowersViewHolder(
            FollowingFollowersItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FollowingFollowersViewHolder, position: Int) {
        val currentPosition = items[position]
        holder.binding.tvName.text = currentPosition.login
        holder.binding.tvUsername.text = currentPosition.login
        Glide.with(holder.itemView.context)
            .load(currentPosition.avatarUrl)
            .into(holder.binding.ivAvatar)
    }

    override fun getItemCount(): Int = items.size

    private fun clearData() {
        this.items.clear()
    }

    fun setData(items: ArrayList<GithubUsersItem>) {
        clearData()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}
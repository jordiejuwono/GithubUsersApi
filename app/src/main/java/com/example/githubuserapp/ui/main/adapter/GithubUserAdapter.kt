package com.example.githubuserapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.databinding.GithubUserItemsBinding
import com.example.githubuserapp.model.users.GithubUsersItem

class GithubUserAdapter(private val intentToProfileDetails: IntentToProfileDetails) : RecyclerView.Adapter<GithubUserAdapter.GithubUserViewHolder>() {

    val items: ArrayList<GithubUsersItem> = arrayListOf()

    interface IntentToProfileDetails {
        fun intentToDetails(githubUsersItem: GithubUsersItem)
    }

    class GithubUserViewHolder(val binding: GithubUserItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder {
        return GithubUserViewHolder(GithubUserItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        val currentPosition = items[position]
        holder.binding.tvName.text = currentPosition.login
        holder.binding.tvUsername.text = currentPosition.login
        Glide.with(holder.itemView.context)
            .load(currentPosition.avatarUrl)
            .circleCrop()
            .into(holder.binding.ivAvatar)
        holder.itemView.setOnClickListener {
            intentToProfileDetails.intentToDetails(currentPosition)
        }
    }

    override fun getItemCount(): Int = items.size

    private fun clearItems () {
        this.items.clear()
    }

    fun setItems(items: ArrayList<GithubUsersItem>) {
        clearItems()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}
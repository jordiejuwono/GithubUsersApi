package com.example.githubuserapp.ui.favorites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.data.room.UsersEntity
import com.example.githubuserapp.databinding.GithubUserItemsBinding

class FavoritesAdapter(private val intentToProfileDetails: IntentToProfileDetails) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    val items: MutableList<UsersEntity> = arrayListOf()

    interface IntentToProfileDetails {
        fun onClick(usersEntity: UsersEntity)
    }

    class FavoritesViewHolder(val binding: GithubUserItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(GithubUserItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val currentPosition = items[position]
        holder.binding.tvUsername.text = currentPosition.login
        holder.binding.tvName.text = currentPosition.name
        Glide.with(holder.itemView.context)
            .load(currentPosition.image)
            .into(holder.binding.ivAvatar)
        holder.itemView.setOnClickListener {
            intentToProfileDetails.onClick(currentPosition)
        }
    }

    override fun getItemCount(): Int = items.size

    private fun clearItems() {
        this.items.clear()
    }

    fun setData(items: List<UsersEntity>) {
        clearItems()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}
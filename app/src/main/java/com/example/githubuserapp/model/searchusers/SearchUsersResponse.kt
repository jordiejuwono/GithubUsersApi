package com.example.githubuserapp.model.searchusers

import com.example.githubuserapp.model.users.GithubUsersItem
import com.google.gson.annotations.SerializedName

data class SearchUsersResponse(
    @SerializedName("incomplete_results")
    var incompleteResults: Boolean?,
    @SerializedName("items")
    var items: ArrayList<GithubUsersItem>?,
    @SerializedName("total_count")
    var totalCount: Int?
)
package com.example.githubuserapp.services

import com.example.githubuserapp.model.details.UserDetailsResponse
import com.example.githubuserapp.model.searchusers.SearchUsersResponse
import com.example.githubuserapp.model.users.GithubUsers
import com.example.githubuserapp.model.users.GithubUsersItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    fun getGithubUsers() : Call<GithubUsers>

    @GET("search/users?")
    fun searchGithubUsers(
        @Query("q") search: String
    ) : Call<SearchUsersResponse>

    @GET("users/{userid}")
    fun getUserDetails(
        @Path("userid") userId: String
    ) : Call<UserDetailsResponse>

    @GET("users/{userid}/following")
    fun getAllFollowing(
        @Path("userid") userId: String
    ) : Call<GithubUsers>

    @GET("users/{userid}/followers")
    fun getAllFollowers(
        @Path("userid") userId: String
    ) : Call<GithubUsers>

}
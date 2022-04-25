package com.example.githubuserapp.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.model.details.UserDetailsResponse
import com.example.githubuserapp.model.searchusers.SearchUsersResponse
import com.example.githubuserapp.model.users.GithubUsers
import com.example.githubuserapp.model.users.GithubUsersItem
import com.example.githubuserapp.services.ApiConfig
import com.example.githubuserapp.ui.detail.UserDetailActivity
import com.example.githubuserapp.ui.main.GithubUserActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _githubUsersList = MutableLiveData<GithubUsers>()
    val githubUsersList: LiveData<GithubUsers> = _githubUsersList

    private val _searchGithubUsers = MutableLiveData<SearchUsersResponse>()
    val searchGithubUsers: LiveData<SearchUsersResponse> = _searchGithubUsers

    private val _noResults = MutableLiveData<Boolean>()
    val noResults: LiveData<Boolean> = _noResults

    private val _userDetails = MutableLiveData<UserDetailsResponse>()
    val userDetails: LiveData<UserDetailsResponse> = _userDetails

    private val _userFollowers = MutableLiveData<GithubUsers>()
    val userFollowers: LiveData<GithubUsers> = _userFollowers

    private val _userFollowing = MutableLiveData<GithubUsers>()
    val userFollowing: LiveData<GithubUsers> = _userFollowing

    fun getAllGithubUsers(context: Context) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGithubUsers()
        client.enqueue(object : Callback<GithubUsers> {
            override fun onResponse(call: Call<GithubUsers>, response: Response<GithubUsers>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _githubUsersList.value = response.body()
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GithubUsers>, t: Throwable) {
                Toast.makeText(context, t.message.orEmpty(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getAllSearchedUsers(context: Context, search: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchGithubUsers(search)
        client.enqueue(object : Callback<SearchUsersResponse> {
            override fun onResponse(
                call: Call<SearchUsersResponse>,
                response: Response<SearchUsersResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _searchGithubUsers.value = response.body()
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }

                if (response.body()?.totalCount == 0) {
                    _noResults.value = true
                } else {
                    _noResults.value = false
                }

            }

            override fun onFailure(call: Call<SearchUsersResponse>, t: Throwable) {
                Toast.makeText(context, t.message.orEmpty(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getUserDetails(context: Context, userId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetails(userId)
        client.enqueue(object : Callback<UserDetailsResponse> {
            override fun onResponse(
                call: Call<UserDetailsResponse>,
                response: Response<UserDetailsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userDetails.value = response.body()
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                Toast.makeText(context, t.message.orEmpty(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getUserFollowers(context: Context, userId: String) {
        val client = ApiConfig.getApiService().getAllFollowers(userId)
        client.enqueue(object : Callback<GithubUsers> {
            override fun onResponse(call: Call<GithubUsers>, response: Response<GithubUsers>) {
                if (response.isSuccessful) {
                    _userFollowers.value = response.body()
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GithubUsers>, t: Throwable) {
                Toast.makeText(context, t.message.orEmpty(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getUserFollowing(context: Context, userId: String) {
        val client = ApiConfig.getApiService().getAllFollowing(userId)
        client.enqueue(object : Callback<GithubUsers> {
            override fun onResponse(call: Call<GithubUsers>, response: Response<GithubUsers>) {
                if (response.isSuccessful) {
                    _userFollowing.value = response.body()
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GithubUsers>, t: Throwable) {
                Toast.makeText(context, t.message.orEmpty(), Toast.LENGTH_SHORT).show()
            }

        })
    }

}
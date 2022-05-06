package com.example.githubuserapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.databinding.FragmentGithubUsersBinding
import com.example.githubuserapp.model.users.GithubUsersItem
import com.example.githubuserapp.ui.detail.UserDetailActivity
import com.example.githubuserapp.ui.main.adapter.GithubUserAdapter
import com.example.githubuserapp.viewmodel.githubusers.GithubViewModel
import com.example.githubuserapp.viewmodel.githubusers.ViewModelFactory

class GithubUsersFragment : Fragment() {

    private var _binding: FragmentGithubUsersBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: GithubUserAdapter
    private lateinit var viewModel: GithubViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGithubUsersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = obtainViewModel(activity as AppCompatActivity)
        getData()
        setRecyclerView()
    }

    private fun obtainViewModel(activity: AppCompatActivity): GithubViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[GithubViewModel::class.java]
    }

    private fun getData() {
        viewModel.getAllGithubUsers(requireContext())
    }

    private fun setData() {
        viewModel.githubUsersList.observe(viewLifecycleOwner) {
            adapter.setItems(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        viewModel.noResults.observe(viewLifecycleOwner) {
            showNoResults(it)
        }
    }

    private fun setRecyclerView() {
        adapter = GithubUserAdapter(object : GithubUserAdapter.IntentToProfileDetails {
            override fun intentToDetails(githubUsersItem: GithubUsersItem) {
                val intent = Intent(requireContext(), UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.USER_DETAILS, githubUsersItem)
                startActivity(intent)
            }

        })
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvGithubUsers.apply {
            this.adapter = this@GithubUsersFragment.adapter
            this.layoutManager = layoutManager
            addItemDecoration(itemDecoration)
        }

        //set data to adapter
        setData()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showNoResults(noResults: Boolean) {
        binding.tvNoResults.visibility = if (noResults) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

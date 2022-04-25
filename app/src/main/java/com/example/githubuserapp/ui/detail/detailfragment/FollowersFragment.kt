package com.example.githubuserapp.ui.detail.detailfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.databinding.FragmentFollowersBinding
import com.example.githubuserapp.ui.detail.adapter.FollowingFollowersAdapter
import com.example.githubuserapp.viewmodel.GithubViewModel

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FollowingFollowersAdapter
    private val viewModel by viewModels<GithubViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        getData()
    }

    private fun setAdapter() {
        adapter = FollowingFollowersAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvFollowers.apply {
            adapter = this@FollowersFragment.adapter
            this.layoutManager = layoutManager
            addItemDecoration(itemDecoration)
        }
        getData()
    }

    private fun getData() {
        viewModel.getUserFollowers(requireContext(), userId)
        viewModel.userFollowers.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var userId: String = ""
    }

}
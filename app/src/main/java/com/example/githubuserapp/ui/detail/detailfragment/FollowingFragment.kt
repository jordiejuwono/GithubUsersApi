package com.example.githubuserapp.ui.detail.detailfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.databinding.FragmentFollowingBinding
import com.example.githubuserapp.ui.detail.adapter.FollowingFollowersAdapter
import com.example.githubuserapp.viewmodel.GithubViewModel

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FollowingFollowersAdapter
    private val viewModel by viewModels<GithubViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
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
        binding.rvFollowing.apply {
            adapter = this@FollowingFragment.adapter
            this.layoutManager = layoutManager
            addItemDecoration(itemDecoration)
        }
        getData()
    }

    private fun getData() {
        viewModel.getUserFollowing(requireContext(), userId)
        viewModel.userFollowing.observe(viewLifecycleOwner) {
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
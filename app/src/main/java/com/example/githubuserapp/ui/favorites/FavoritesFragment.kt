package com.example.githubuserapp.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.data.room.UsersEntity
import com.example.githubuserapp.databinding.FragmentFavoritesBinding
import com.example.githubuserapp.ui.detail.UserDetailActivity
import com.example.githubuserapp.ui.favorites.adapter.FavoritesAdapter
import com.example.githubuserapp.viewmodel.githubusers.GithubViewModel
import com.example.githubuserapp.viewmodel.githubusers.ViewModelFactory

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GithubViewModel
    private lateinit var adapter: FavoritesAdapter
    private var usersList = listOf<UsersEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = obtainViewModel(activity as AppCompatActivity)
        setAdapter()
    }

    private fun obtainViewModel(activity: AppCompatActivity): GithubViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[GithubViewModel::class.java]
    }

    private fun setAdapter() {
        adapter = FavoritesAdapter(object : FavoritesAdapter.IntentToProfileDetails {
            override fun onClick(usersEntity: UsersEntity) {
                UserDetailActivity.startActivityFavorites(
                    requireContext(),
                    UserDetailActivity.FROM_FAVORITES,
                    usersEntity
                )
            }

        })
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)

        binding.rvFavorites.apply {
            this.adapter = this@FavoritesFragment.adapter
            this.layoutManager = layoutManager
            addItemDecoration(itemDecoration)
        }

        //set data to recycler view
        setData()

    }

    private fun setData() {
        viewModel.getAllFavorites().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.tvNoFavorites.visibility = View.VISIBLE
            } else {
                binding.tvNoFavorites.visibility = View.GONE
            }
            adapter.setData(it)
            usersList = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
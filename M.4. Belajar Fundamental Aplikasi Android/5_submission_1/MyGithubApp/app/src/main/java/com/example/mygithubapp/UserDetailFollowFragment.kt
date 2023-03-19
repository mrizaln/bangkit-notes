package com.example.mygithubapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubapp.databinding.FragmentUserDetailFollowBinding

class UserDetailFollowFragment : Fragment() {
    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = "app_name"
    }

    private lateinit var binding: FragmentUserDetailFollowBinding
    private val userDetailFollowViewModel by viewModels<UserDetailFollowViewModel>()

    private var position: Int? = null
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserDetailFollowBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        userDetailFollowViewModel.isSuccessLoadUsers.observe(viewLifecycleOwner) { isSuccess ->
            if (!isSuccess) {
                Toast.makeText(
                    view.context, "Failed to load data", Toast.LENGTH_LONG
                ).show()
            }
        }

        userDetailFollowViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarDetailFollow.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        userDetailFollowViewModel.listUser.observe(viewLifecycleOwner) { listUserResponse ->
            setUserList(listUserResponse)
        }

        if (position == 1) {
            userDetailFollowViewModel.getUsers(
                username ?: "", UserDetailActivity.Companion.Follow.FOLLOWERS
            )
        } else {
            userDetailFollowViewModel.getUsers(
                username ?: "", UserDetailActivity.Companion.Follow.FOLLOWING
            )
        }
    }

    private fun setUserList(listUsersResponse: List<UserItemResponse?>) {
        val listUser = ArrayList<UserListData>()
        for (userResponse in listUsersResponse) {
            if (userResponse == null) continue

            val username = userResponse.login
            val avatar = userResponse.avatarUrl

            val userListData = UserListData(
                username, avatar
            )
            listUser.add(userListData)
        }
        val adapter = UserListAdapter(listUser)
        binding.rvFollow.adapter = adapter

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
    }
}
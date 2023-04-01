package com.example.mygithubapp2.ui.detail.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubapp2.data.local.entity.UserEntity
import com.example.mygithubapp2.data.remote.RequestResult
import com.example.mygithubapp2.databinding.FragmentUserDetailFollowBinding
import com.example.mygithubapp2.ui.UserListAdapter
import com.example.mygithubapp2.ui.ViewModelFactory

class UserDetailFollowFragment : Fragment() {
    enum class Arguments(val key: String) {
        POSITION("position"),
        USERNAME("username")
    }

    private lateinit var binding: FragmentUserDetailFollowBinding
    private val userDetailFollowViewModel by viewModels<UserDetailFollowViewModel> {
        ViewModelFactory.getInstance(requireView().context)
    }

    private var position: Int = 0
    private lateinit var username: String

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

        requireArguments().let {
            position = it.getInt(Arguments.POSITION.key)
            username = it.getString(Arguments.USERNAME.key)!!
        }

        if (position == 1) {
            userDetailFollowViewModel.getFollowers(username)
                .observe(viewLifecycleOwner) { result ->
                    observer(view, result)
                }
        } else {
            userDetailFollowViewModel.getFollowing(username)
                .observe(viewLifecycleOwner) { result ->
                    observer(view, result)
                }
        }
    }

    private fun observer(view: View, result: RequestResult<List<UserEntity>>) {
        when (result) {
            is RequestResult.Loading -> binding.progressBarDetailFollow.visibility = View.VISIBLE
            is RequestResult.Success -> {
                binding.progressBarDetailFollow.visibility = View.GONE
                setUserList(result.data)
            }
            is RequestResult.Error -> {
                binding.progressBarDetailFollow.visibility = View.GONE
                Toast.makeText(
                    view.context, "Failed to load data: ${result.error}", Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setUserList(listUser: List<UserEntity>) {
        val adapter = UserListAdapter(listUser)
        binding.rvFollow.adapter = adapter

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
    }
}
package com.example.mygithubapp2.ui.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mygithubapp2.data.UserRepository
import com.example.mygithubapp2.data.local.entity.UserEntity
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class FavoriteViewModelTest {
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var userRepository: UserRepository

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dummyListUser = listOf(
        UserEntity("mrizaln", "https://avatars.githubusercontent.com/u/61090869?v=4"),
        UserEntity("Lunozt", "https://avatars.githubusercontent.com/u/25452701?v=4"),
        UserEntity("CyanNyan", "https://avatars.githubusercontent.com/u/75556638?v=4"),
    )

    @Before
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        favoriteViewModel = FavoriteViewModel(userRepository)
    }

    @Test
    fun getUsers() {
        val mockLiveData = MutableLiveData<List<UserEntity>>(dummyListUser)
        `when`(userRepository.getFavoriteUsers()).thenReturn(mockLiveData)

        val result = favoriteViewModel.getUsers()

        assertEquals(dummyListUser, result.value)
    }

    @Test
    fun removeUsers() {
    }
}
package com.example.mystoryapp2.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.mystoryapp2.model.data.local.database.Story
import com.example.mystoryapp2.model.repository.AppRepository
import com.example.mystoryapp2.ui.adapter.StoryListAdapter
import com.example.mystoryapp2.util.DataDummy
import com.example.mystoryapp2.util.MainDispatcherRule
import com.example.mystoryapp2.util.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: AppRepository

    @Test
    fun `when getStories success and returns data`() = runTest {
        val dummyStories = DataDummy.generateDummyStories()
        val dummyData = PagingData.from(dummyStories)
        val expectedData = MutableLiveData<PagingData<Story>>()
        expectedData.value = dummyData
        Mockito.`when`(repository.getPagedStories()).thenReturn(expectedData)

        val viewModel = MainViewModel(repository)
        val actualData = viewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.diffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualData)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertTrue(dummyStories[0].hasSameContent(differ.snapshot()[0]!!))      // never null if above asserts success
    }

    @Test
    fun `when getStories success but return empty data`() = runTest {
        val expectedData = MutableLiveData<PagingData<Story>>()
        expectedData.value = PagingData.from(emptyList())
        Mockito.`when`(repository.getPagedStories()).thenReturn(expectedData)

        val viewModel = MainViewModel(repository)
        val actualData = viewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.diffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualData)
        assertEquals(0, differ.snapshot().size)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    private fun Story.hasSameContent(other: Story): Boolean {
        // @formatter:off
        return (this.id == other.id)
                && (this.name == other.name)
                && (this.description == other.description)
                && (this.createdAt == other.createdAt)
                && (this.lat == other.lat) && (this.lon == other.lon)
        // @formatter:on
    }
}
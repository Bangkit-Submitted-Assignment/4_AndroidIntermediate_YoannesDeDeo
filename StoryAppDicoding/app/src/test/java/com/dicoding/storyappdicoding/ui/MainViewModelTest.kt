package com.dicoding.storyappdicoding.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.storyappdicoding.DataDummy
import com.dicoding.storyappdicoding.MainDispatcherRule
import com.dicoding.storyappdicoding.adapter.MainAdapter
import com.dicoding.storyappdicoding.api.ListStoryItem
import com.dicoding.storyappdicoding.getOrAwaitValue
import com.dicoding.storyappdicoding.repository.UserRepository
import com.dicoding.storyappdicoding.view_model.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository

    private val token=DataDummy().dummyToken()

    @Test
    fun `when Get story Should Not Null and Return Data`() = runTest {
        val dummy = DataDummy().generateDummyResponse()
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummy)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data
        Mockito.`when`(userRepository.getStoryPaging(token)).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(userRepository)
        val actualStory: PagingData<ListStoryItem> = mainViewModel.getStoryPaging(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MainAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummy.size, differ.snapshot().size)
        Assert.assertEquals(dummy[0], differ.snapshot()[0])
    }
    @Test
    fun `when Get story Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data
        Mockito.`when`(userRepository.getStoryPaging(token)).thenReturn(expectedStory)
        val mainViewModel = MainViewModel(userRepository)
        val actualStory: PagingData<ListStoryItem> = mainViewModel.getStoryPaging(token).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = MainAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, ListStoryItem>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}



    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

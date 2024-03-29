package com.dicoding.storyappdicoding.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyappdicoding.api.DetailResponse
import com.dicoding.storyappdicoding.api.ListStoryItem
import com.dicoding.storyappdicoding.data_class.DataUser
import com.dicoding.storyappdicoding.di.Helper
import com.dicoding.storyappdicoding.repository.UserRepository
import kotlinx.coroutines.launch


class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<DataUser> {
        return repository.getSession().asLiveData()
    }

    private val storyDetailData = MutableLiveData<Helper<DetailResponse>>()
    val getStoryDetailData: LiveData<Helper<DetailResponse>> = storyDetailData

    fun getStoryPaging(token: String): LiveData<PagingData<ListStoryItem>> = repository.getStoryPaging(token).cachedIn(viewModelScope)

    fun getDetail(token: String, id: String) {
        viewModelScope.launch {
            repository.getDetailUser(token, id).asFlow().collect {
                storyDetailData.value = it
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}
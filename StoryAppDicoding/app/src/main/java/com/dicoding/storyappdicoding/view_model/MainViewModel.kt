package com.dicoding.storyappdicoding.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyappdicoding.api.ListStoryItem
import com.dicoding.storyappdicoding.data_class.DataUser
import com.dicoding.storyappdicoding.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val storyResponse = MutableLiveData<List<ListStoryItem?>>()
    fun getSession(): LiveData<DataUser> {
        return repository.getSession().asLiveData()
    }

    fun getStoryResponse(): LiveData<List<ListStoryItem?>> {
        return storyResponse
    }

    fun getStory() {
        viewModelScope.launch {
            try {
                val response = repository.getStory()
                val stories = response.listStory ?: emptyList()
                storyResponse.postValue(stories)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Permintaan memuat data gagal")
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}
package com.dicoding.storyappdicoding.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyappdicoding.api.StoryResponse
import com.dicoding.storyappdicoding.data_class.DataUser
import com.dicoding.storyappdicoding.di.Helper
import com.dicoding.storyappdicoding.repository.UserRepository
import kotlinx.coroutines.launch

class MapViewModel(private val userRepository: UserRepository):ViewModel() {
    private val storyLiveData = MutableLiveData<Helper<StoryResponse>>()
    val getStoryLiveData: LiveData<Helper<StoryResponse>> = storyLiveData

    fun getLocation(author:String){
        viewModelScope.launch {
            userRepository.getLocation(author).asFlow().collect {
                storyLiveData.value = it
            }
        }
    }

    fun getSession(): LiveData<DataUser> {
        return userRepository.getSession().asLiveData()
    }
}
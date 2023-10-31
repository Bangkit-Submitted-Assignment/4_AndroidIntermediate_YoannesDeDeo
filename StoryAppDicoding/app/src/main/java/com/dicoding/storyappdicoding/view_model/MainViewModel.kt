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


class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<DataUser> {
        return repository.getSession().asLiveData()
    }

    private val storyLiveData = MutableLiveData<Helper<StoryResponse>>()
    val getStoryLiveData : LiveData<Helper<StoryResponse>> = storyLiveData
fun getStory(token: String) {
    viewModelScope.launch {
        repository.getStory(token).asFlow().collect{
            storyLiveData.value=it
        }
    }
}

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}
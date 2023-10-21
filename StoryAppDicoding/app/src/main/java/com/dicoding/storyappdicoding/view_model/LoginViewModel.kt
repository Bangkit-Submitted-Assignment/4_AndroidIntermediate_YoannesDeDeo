package com.dicoding.storyappdicoding.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyappdicoding.data_class.DataUser
import com.dicoding.storyappdicoding.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: DataUser) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}
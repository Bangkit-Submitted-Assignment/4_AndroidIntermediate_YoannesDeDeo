package com.dicoding.storyappdicoding.repository

import com.dicoding.storyappdicoding.api.ApiService
import com.dicoding.storyappdicoding.api.LoginResponse
import com.dicoding.storyappdicoding.api.RegisterResponse
import com.dicoding.storyappdicoding.api.StoryResponse
import com.dicoding.storyappdicoding.data_class.DataUser
import com.dicoding.storyappdicoding.data_class.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService)
{

    suspend fun saveSession(user: DataUser) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<DataUser> {
        return userPreference.getSession()
    }

    suspend fun getStory(): StoryResponse {
        return apiService.getStories()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(name:String, email:String, password:String,): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login (email:String, password:String): LoginResponse {
        return apiService.login(email,password)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }

}
package com.dicoding.storyappdicoding.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.dicoding.storyappdicoding.api.ApiService
import com.dicoding.storyappdicoding.api.LoginResponse
import com.dicoding.storyappdicoding.api.RegisterResponse
import com.dicoding.storyappdicoding.data_class.DataUser
import com.dicoding.storyappdicoding.data_class.UserPreference
import com.dicoding.storyappdicoding.di.Helper
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

    suspend fun getStory(token: String)= liveData{
        try {
            val response= apiService.getStories("Bearer $token")
            emit(Helper.Success(response))
        }catch (e:Exception){
            Log.d("User repository", "Permintaan memuat data gagal", e)
        }
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
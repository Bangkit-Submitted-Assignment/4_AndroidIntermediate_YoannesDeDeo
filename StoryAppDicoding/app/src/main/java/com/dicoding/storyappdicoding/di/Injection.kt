package com.dicoding.storyappdicoding.di
import android.content.Context
import com.dicoding.storyappdicoding.api.ApiConfig
import com.dicoding.storyappdicoding.data_class.UserPreference
import com.dicoding.storyappdicoding.data_class.dataStore
import com.dicoding.storyappdicoding.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val tokenFlow = UserPreference.getInstance(context.dataStore)
        val apiService= ApiConfig.getApiService()
        return UserRepository.getInstance(tokenFlow,apiService)
    }
}
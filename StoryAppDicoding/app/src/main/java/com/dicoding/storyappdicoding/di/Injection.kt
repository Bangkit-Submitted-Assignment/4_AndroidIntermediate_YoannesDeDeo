package com.dicoding.storyappdicoding.di
import android.content.Context
import com.dicoding.storyappdicoding.api.ApiConfig
import com.dicoding.storyappdicoding.api.ApiService
import com.dicoding.storyappdicoding.data_class.UserPreference
import com.dicoding.storyappdicoding.data_class.dataStore
import com.dicoding.storyappdicoding.repository.UserRepository

object Injection {
    private val apiService: ApiService by lazy {
        ApiConfig().getApiService()
    }

    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = provideApiService()
        return UserRepository.getInstance(pref, apiService)

    }
    private fun provideApiService(): ApiService {
            return apiService
    }
}
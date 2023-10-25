package com.dicoding.storyappdicoding.di
import android.content.Context
import android.util.Log
import com.dicoding.storyappdicoding.api.ApiConfig
import com.dicoding.storyappdicoding.api.ApiService
import com.dicoding.storyappdicoding.data_class.UserPreference
import com.dicoding.storyappdicoding.data_class.dataStore
import com.dicoding.storyappdicoding.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val tokenFlow = UserPreference.getInstance(context.dataStore).getSession()
        val token: String? = runBlocking {
            tokenFlow.first().token
        }

        val apiConfig = ApiConfig()

        val apiService: ApiService? = if (token != null) {
            apiConfig.getApiService(token)
        } else {
            Log.d("Injection", "Token tidak tersedia.")
            null
        }

        if (apiService != null) {
            val pref = UserPreference.getInstance(context.dataStore)
            return UserRepository.getInstance(pref, apiService)
        } else {
            Log.e("Injection", "Gagal membuat ApiService karena token tidak tersedia.")
            return UserRepository.getInstance(UserPreference.getInstance(context.dataStore), apiConfig.getApiService(""))
        }
    }
}
package com.dicoding.storyappdicoding.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dicoding.storyappdicoding.api.ErrorResponse
import com.dicoding.storyappdicoding.data_class.DataUser
import com.dicoding.storyappdicoding.repository.UserRepository
import com.google.gson.Gson
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    var successMessage: String? = null

    suspend fun login(email: String, password: String) {
        try {
            Log.d("loginViewModel", "Email yang akan dikirim ke server: $email")

            val response = repository.login(email, password)

            Log.d("loginViewModel", "Permintaan login berhasil.")

            successMessage = response.message

            if (successMessage != null) {
                val user = DataUser(email, response.loginResult?.token ?: "", isLogin = true)
                repository.saveSession(user)
            }

        } catch (e: HttpException) {
            Log.e("loginViewModel", "Kesalahan saat melakukan permintaan login: ${e.message()}")

            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
        }
    }
}

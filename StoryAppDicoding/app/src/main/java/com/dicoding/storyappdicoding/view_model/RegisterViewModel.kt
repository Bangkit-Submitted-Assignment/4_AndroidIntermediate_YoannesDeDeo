package com.dicoding.storyappdicoding.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dicoding.storyappdicoding.api.ErrorResponse
import com.dicoding.storyappdicoding.repository.UserRepository
import com.google.gson.Gson
import retrofit2.HttpException

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    var successMessage: String? = null

    suspend fun registerUser(name: String, email: String, password: String) {
        try {
            Log.d("RegisterViewModel", "Email yang akan dikirim ke server: $email")

            val response = repository.register(name, email, password)

            Log.d("RegisterViewModel", "Permintaan pendaftaran berhasil.")

            successMessage = response.message

        } catch (e: HttpException) {
            Log.e(
                "RegisterViewModel",
                "Kesalahan saat melakukan permintaan pendaftaran: ${e.message()}"
            )

            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            errorBody.message
        }
    }

}
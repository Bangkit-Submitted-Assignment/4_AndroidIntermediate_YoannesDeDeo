package com.dicoding.storyappdicoding

sealed class Result {
    data class Success(val message: String) : Result()
    data class Error(val errorMessage: String) : Result()
}
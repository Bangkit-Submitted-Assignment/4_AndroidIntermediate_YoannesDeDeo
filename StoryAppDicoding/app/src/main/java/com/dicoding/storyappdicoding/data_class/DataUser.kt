package com.dicoding.storyappdicoding.data_class


data class DataUser (
    val email: String,
    val token: String,
    var isLogin: Boolean = false
)

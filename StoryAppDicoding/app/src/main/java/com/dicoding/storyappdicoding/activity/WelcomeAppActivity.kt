package com.dicoding.storyappdicoding.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyappdicoding.databinding.ActivityWelcomeAppBinding

class WelcomeAppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        clickAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun clickAction() {
        binding.loginButton.setOnClickListener {
            val goLoginActivity=Intent(this@WelcomeAppActivity,LoginActivity::class.java)
            startActivity(goLoginActivity)
        }
        binding.signupButton.setOnClickListener {
            val goRegisActivity=Intent(this@WelcomeAppActivity,RegisterActivity::class.java)
            startActivity(goRegisActivity)
        }
    }
}
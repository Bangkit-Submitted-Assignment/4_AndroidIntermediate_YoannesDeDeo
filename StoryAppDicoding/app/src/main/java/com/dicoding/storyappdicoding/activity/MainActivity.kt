package com.dicoding.storyappdicoding.activity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyappdicoding.R
import com.dicoding.storyappdicoding.di.Injection
import com.dicoding.storyappdicoding.view_model.MainViewModel
import com.dicoding.storyappdicoding.view_model_factory.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(Injection.provideRepository(this))
        ).get(MainViewModel::class.java)

        getSession()
    }

    private fun getSession(){
        mainViewModel.getSession().observe(this){user->
            if (!user.isLogin){
                AlertDialog.Builder(this@MainActivity).apply {
                    setTitle("warning")
                    setMessage("anda belum login atau session telah berakhir. silahkan login dulu")
                    setPositiveButton("Lanjut") { _, _ ->
                        finish()
                    }
                    create()
                    show()
                }
            }
        }
    }

}
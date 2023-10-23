package com.dicoding.storyappdicoding.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.buttonLogout -> {
                mainViewModel.logout()
                val goLogout = Intent(this@MainActivity, WelcomeAppActivity ::class.java)
                startActivity(goLogout)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }



}
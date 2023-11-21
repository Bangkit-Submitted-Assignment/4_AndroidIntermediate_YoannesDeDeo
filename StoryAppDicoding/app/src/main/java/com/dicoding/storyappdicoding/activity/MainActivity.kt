package com.dicoding.storyappdicoding.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyappdicoding.R
import com.dicoding.storyappdicoding.adapter.LoadingStateAdapter
import com.dicoding.storyappdicoding.adapter.MainAdapter
import com.dicoding.storyappdicoding.databinding.ActivityMainBinding
import com.dicoding.storyappdicoding.di.Injection
import com.dicoding.storyappdicoding.view_model.MainViewModel
import com.dicoding.storyappdicoding.view_model_factory.ViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.beranda)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(Injection.provideRepository(this))
        ).get(MainViewModel::class.java)

        cekSession()
        showData()

        binding.uploadButton.setOnClickListener {
            val intent = Intent(this@MainActivity, UploadActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showData(){
        if (isNetworkAvailable()){
            showLoading(true)
            showingListStory()
        }else{
            showLoading(true)
            AlertDialog.Builder(this).apply {
                setTitle("No Internet Connection")
                setMessage("enable internet connectivity to continue.")
                setPositiveButton("Enable Internet") { _, _ ->
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
                setNegativeButton("Cancel") { _, _ ->
                    showToast("Please enable internet to use the app.")
                }
                create()
                show()
            }
            showToast(getString(R.string.kosong))
        }
        showLoading(false)
    }

    private fun showingListStory() {
        val layoutManager = LinearLayoutManager(this)
        binding.listRecycleMain.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.listRecycleMain.addItemDecoration(itemDecoration)

        val tokenFlow = mainViewModel.getSession()
        tokenFlow.observe(this) { user ->
            val token = user.token
            val adapter = MainAdapter()
            binding.listRecycleMain.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            mainViewModel.getStoryPaging(token).observe(this) { stories ->
                if (stories != null) {
                    adapter.submitData(lifecycle, stories)
                } else {
                    showToast(getString(R.string.kosong))
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun cekSession() {
        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                AlertDialog.Builder(this).apply {
                    setTitle("PERHATIAN")
                    setMessage("Anda harus login dahulu atau register sebelum masuk ke aplikasi")
                    setPositiveButton("OKE") { _, _ ->
                        val login = Intent(this@MainActivity, WelcomeAppActivity::class.java)
                        startActivity(login)
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
                val goLogout = Intent(this@MainActivity, WelcomeAppActivity::class.java)
                startActivity(goLogout)
                return true
            }

            R.id.buttonMaps -> {
                val goMaps = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(goMaps)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Keluar")
            setMessage("Anda yakin ingin keluar dari aplikasi?")
            setPositiveButton("Ya") { _, _ ->
                finishAffinity()
            }
            setNegativeButton("Tidak", null)
            create()
            show()
        }
    }
}


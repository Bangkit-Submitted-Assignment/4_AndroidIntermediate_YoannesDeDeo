package com.dicoding.storyappdicoding.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyappdicoding.R
import com.dicoding.storyappdicoding.adapter.MainAdapter
import com.dicoding.storyappdicoding.api.ListStoryItem
import com.dicoding.storyappdicoding.databinding.ActivityMainBinding
import com.dicoding.storyappdicoding.databinding.ListItemBinding
import com.dicoding.storyappdicoding.di.Helper
import com.dicoding.storyappdicoding.di.Injection
import com.dicoding.storyappdicoding.view_model.MainViewModel
import com.dicoding.storyappdicoding.view_model_factory.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var itemBinding: ListItemBinding


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

        val layoutManager = LinearLayoutManager(this)
        binding.listRecycleMain.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.listRecycleMain.addItemDecoration(itemDecoration)

        val tokenFlow = mainViewModel.getSession()
        tokenFlow.observe(this) { user ->
            val token = user.token
            mainViewModel.getStory(token)
            mainViewModel.getStoryLiveData.observe(this) { stories ->
                if (stories != null) {
                    when (stories) {
                        is Helper.Success -> setStory(stories.data.listStory)
                        is Helper.Error -> showError("Failed to load data: ${stories.eror.message}")
                    }

                }else{
                    itemBinding.itemDescription.text=getString(R.string.kosong)
                    itemBinding.itemName.text=getString(R.string.kosong)
                }
            }
        }

        binding.uploadButton.setOnClickListener{
            val intent= Intent(this@MainActivity,UploadActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setStory(data: List<ListStoryItem>) {
        val adapter = MainAdapter(this)
        adapter.submitList(data)
        binding.listRecycleMain.adapter = adapter
    }

    private fun showError(message: String) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("Failed to load data: $message")
            .setPositiveButton("Retry") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
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
            R.id.buttonMaps ->{
                val goMaps = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(goMaps)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
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


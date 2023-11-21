package com.dicoding.storyappdicoding.activity

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.storyappdicoding.R
import com.dicoding.storyappdicoding.api.Story
import com.dicoding.storyappdicoding.databinding.ActivityDetailBinding
import com.dicoding.storyappdicoding.di.Helper
import com.dicoding.storyappdicoding.di.Injection
import com.dicoding.storyappdicoding.view_model.MainViewModel
import com.dicoding.storyappdicoding.view_model_factory.ViewModelFactory


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: MainViewModel
    var token: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(Injection.provideRepository(this))
        ).get(MainViewModel::class.java)

        val user = intent.getStringExtra(USER).toString()
        val name= intent.getStringExtra(NAME).toString()

        supportActionBar?.title="Show Detail - $name"

        val tokenFlow = viewModel.getSession()
        if (isNetworkAvailable()){
            if(user.isNotEmpty()){
                tokenFlow.observe(this) { userSession ->
                    val token = userSession.token
                    viewModel.getDetail(token, user)
                    viewModel.getStoryDetailData.observe(this) { detailResponse ->
                        when (detailResponse) {
                            is Helper.Success -> showDetail(detailResponse.data.story)
                            is Helper.Error -> showError("Failed to load data: ${detailResponse.eror.message}")
                        }
                    }
                }
            }
        }else{
            showLoading(true)
            binding.apply {
                nameUser.text=getString(R.string.kosong)
                deskripsi.text=getString(R.string.kosong)
                Glide.with(this@DetailActivity)
                    .load(R.drawable.image_welcome)
                    .into(imgItemPhoto)
            }
            showLoading(false)
            showToast(getString(R.string.kosong))
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

    private fun showDetail(data: Story?) {
        showLoading(true)
        binding.apply {
            nameUser.text=data?.name
            deskripsi.text=data?.description
            Glide.with(this@DetailActivity)
                .load(data?.photoUrl)
                .into(imgItemPhoto)
        }
        showLoading(false)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val USER = "user"
        const val NAME="name"
    }
}
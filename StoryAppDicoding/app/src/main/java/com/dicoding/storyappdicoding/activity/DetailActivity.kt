package com.dicoding.storyappdicoding.activity

import android.os.Bundle
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

    private fun showDetail(data: Story?) {
        Glide.with(this)
            .load(data?.photoUrl)
            .into(binding.imgItemPhoto)
        binding.name.text = data?.name
        binding.deskripsi.text = data?.description
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

    companion object {
        const val USER = "user"
        const val NAME="name"
    }
}
package com.dicoding.storyappdicoding.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyappdicoding.activity.DetailActivity
import com.dicoding.storyappdicoding.activity.MainActivity
import com.dicoding.storyappdicoding.api.ListStoryItem
import com.dicoding.storyappdicoding.databinding.ListItemBinding

class MainAdapter(mainActivity: MainActivity) :
    ListAdapter<ListStoryItem, MainAdapter.MyViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener{
            val sendData= Intent(holder.itemView.context,DetailActivity::class.java)
            sendData.putExtra(DetailActivity.USER,user.id)
            sendData.putExtra(DetailActivity.NAME,user.name)
            holder.itemView.context.startActivity(sendData, ActivityOptionsCompat.makeSceneTransitionAnimation(holder.itemView.context as Activity).toBundle())
        }
    }

    inner class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ListStoryItem) {
            val img = review.photoUrl
            Glide.with(binding.root.context)
                .load(img)
                .into(binding.itemPhoto)
            binding.itemName.text = "${review.name}"
            binding.itemDescription.text = review.description
            binding.itemTgl.text=review.createdAt
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
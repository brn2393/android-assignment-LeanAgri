package com.brn2393.assignment.leanagri.domain.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brn2393.assignment.leanagri.R
import com.brn2393.assignment.leanagri.presentation.main.MoviesListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@BindingAdapter("setupAdapter")
fun RecyclerView.setupAdapter(adapter: MoviesListAdapter) {
    this.layoutManager = LinearLayoutManager(this.context)
    this.adapter = adapter
}

@BindingAdapter("imageUrl")
fun ImageView.imageUrl(imageUrl: String) {
    Glide.with(this.context)
        .load("https://image.tmdb.org/t/p/w500/$imageUrl")
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .skipMemoryCache(false)
        .error(R.drawable.ic_launcher_background)
        .placeholder(R.drawable.ic_launcher_background)
        .into(this)
}
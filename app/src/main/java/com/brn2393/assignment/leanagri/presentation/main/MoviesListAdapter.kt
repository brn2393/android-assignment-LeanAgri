package com.brn2393.assignment.leanagri.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brn2393.assignment.leanagri.data.remote.models.TmdbMovie
import com.brn2393.assignment.leanagri.databinding.ItemMainMovieBinding

class MoviesListAdapter : RecyclerView.Adapter<MoviesListAdapter.GithubUserViewHolder>() {

    var movies: List<TmdbMovie> = emptyList()
        set(list) {
            field = list
            notifyDataSetChanged()
        }

    lateinit var mainViewModel: MainViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMainMovieBinding.inflate(layoutInflater, parent, false)
        return GithubUserViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) =
        holder.bind(movies[position], mainViewModel)

    class GithubUserViewHolder(private val dataBinding: ItemMainMovieBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(tmdbMovie: TmdbMovie, mainViewModel: MainViewModel) {
            dataBinding.tmdbMovie = tmdbMovie
            dataBinding.mainViewModel = mainViewModel
            dataBinding.executePendingBindings()
        }
    }
}
package com.example.movielist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class MovieAdapter // Constructor
    (private val movieList: MutableList<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder?>() {
    // added to enable swipe delete
    val swipeToDeleteCallback = SwipeToDeleteCallback()
    // ViewHolder class - embedded class
    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define elements
        var titleTextView: TextView
        var yearTextView: TextView
        var genreTextView: TextView
        var ratingTextView: TextView

        init {
            titleTextView = itemView.findViewById<TextView?>(R.id.titleTextView)
            yearTextView = itemView.findViewById<TextView?>(R.id.yearTextView)
            genreTextView = itemView.findViewById<TextView?>(R.id.genreTextView)
            ratingTextView = itemView.findViewById<TextView?>(R.id.ratingTextView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    // binds view holder to one particular MovieList item
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val Movie = movieList.get(position)
        holder.titleTextView.setText(Movie.title)
        holder.yearTextView.setText(Movie.year)
        holder.genreTextView.setText(Movie.genre)
        holder.ratingTextView.setText(Movie.rating)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
    fun removeItem(position: Int) {
        movieList.removeAt(position)
        // use parent class RecyclerView.ViewHolder function to notify of change
        notifyItemRemoved(position)
    }
    // implement to achieve swipe-delete
    /*******************/
    inner class SwipeToDeleteCallback :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ) =
            if (viewHolder is MovieViewHolder) {
                makeMovementFlags(
                    ItemTouchHelper.ACTION_STATE_IDLE,
                    ItemTouchHelper.RIGHT
                ) or makeMovementFlags(
                    ItemTouchHelper.ACTION_STATE_SWIPE,
                    ItemTouchHelper.RIGHT
                )
            } else {
                0
            }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            removeItem(position)
        }
    }
    /***************************/
}
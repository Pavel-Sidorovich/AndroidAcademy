package com.pavesid.androidacademy.ui.movies

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.Movie
import com.pavesid.androidacademy.utils.setShaderForGradient
import java.util.Collections

internal class MoviesAdapter(private val listener: (Parcelable) -> Unit) :
    RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>(), ItemTouchHelperAdapter {

    private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.hashCode() == newItem.hashCode()
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var movies: List<Movie>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder =
        MoviesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.movie_item,
                parent,
                false
            ),
            listener = listener
        )

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) =
        holder.bind(movie = movies[position])

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        val mutableMovies = movies.toMutableList()
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(mutableMovies, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(mutableMovies, i, i - 1)
            }
        }
        movies = mutableMovies

        return true
    }

    override fun onItemDismiss(position: Int) {
        val mutableMovies = movies.toMutableList()
        mutableMovies.removeAt(position)
        movies = mutableMovies
    }

    class MoviesViewHolder(itemView: View, private val listener: (Parcelable) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val name = itemView.findViewById<TextView>(R.id.movie_name)
        private val pg = itemView.findViewById<TextView>(R.id.movie_rectangle_pg)
        private val duration = itemView.findViewById<TextView>(R.id.movie_duration)
        private val tags = itemView.findViewById<TextView>(R.id.movie_tag)
        private val reviews = itemView.findViewById<TextView>(R.id.movie_reviews)
        private val origImage = itemView.findViewById<ImageView>(R.id.movie_orig)
        private val rating =
            itemView.findViewById<me.zhanghai.android.materialratingbar.MaterialRatingBar>(R.id.movie_rating)

        private var movie: Movie? = null

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(movie: Movie) {
            this.movie = movie
            pg.text = itemView.context.getString(R.string.pg, movie.minimumAge)
            origImage.load(movie.poster) {
                crossfade(true)
                transformations(RoundedCornersTransformation(14f, 14f, 0f, 0f))
            }
            tags.text = movie.genres.take(MAX_GENRE).joinToString { it.name }
            rating.rating = movie.ratings / 2
            reviews.text = itemView.context.resources.getQuantityString(
                R.plurals.review,
                movie.numberOfRatings,
                movie.numberOfRatings
            )
            duration.text = itemView.context.getString(R.string.duration, movie.runtime)
            name.text = movie.title
            name.setShaderForGradient()
        }

        override fun onClick(p0: View?) {
            movie?.let(listener)
        }
    }

    private companion object {
        private const val MAX_GENRE = 4
    }
}

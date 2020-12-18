package com.pavesid.androidacademy.ui.movies

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.Movie
import com.pavesid.androidacademy.databinding.MovieItem2Binding
import com.pavesid.androidacademy.databinding.MovieItemBinding
import com.pavesid.androidacademy.utils.extensions.setSafeOnClickListener
import com.pavesid.androidacademy.utils.extensions.setShaderForGradient
import java.util.Collections

internal class MoviesAdapter(private val listener: (Parcelable, Int, Int) -> Unit) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder = if (viewType == 1) {
        MoviesViewHolderV1(
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener = listener
        )
    } else {
        MoviesViewHolderV2(
            MovieItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener = listener
        )
    }

    override fun getItemViewType(position: Int): Int {
        return movies[position].id % 2
    }

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

    class MoviesViewHolderV1(
        private val binding: MovieItemBinding,
        private val listener: (Parcelable, Int, Int) -> Unit
    ) : MoviesViewHolder(binding.root) {

        override fun bind(movie: Movie) {
            binding.apply {
                movieRectanglePg.text = itemView.context.getString(R.string.pg, movie.minimumAge)
                movieOrig.load(movie.poster) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation(14f, 14f, 0f, 0f))
                }
                movieTag.text = movie.genres.take(MAX_GENRE).joinToString { it.name }
                movieRating.rating = movie.ratings / 2
                movieReviews.text = itemView.context.resources.getQuantityString(
                    R.plurals.review,
                    movie.numberOfRatings,
                    movie.numberOfRatings
                )
                movieDuration.text = itemView.context.getString(R.string.duration, movie.runtime)
                movieName.text = movie.title
                movieName.setShaderForGradient()
            }

            binding.root.setSafeOnClickListener { view ->
                val cX = (view.left + view.right) / 2
                val cY = (view.top + view.bottom) / 2
                listener(movie, cX, cY)
            }
        }
    }

    class MoviesViewHolderV2(
        private val binding: MovieItem2Binding,
        private val listener: (Parcelable, Int, Int) -> Unit
    ) : MoviesViewHolder(binding.root) {

        override fun bind(movie: Movie) {
            binding.apply {
                movieRectanglePg.text = itemView.context.getString(R.string.pg, movie.minimumAge)
                movieOrig.load(movie.poster) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation(0f, 0f, 14f, 14f))
                }
                movieTag.text = movie.genres.take(MAX_GENRE).joinToString { it.name }
                movieRating.rating = movie.ratings / 2
                movieReviews.text = itemView.context.resources.getQuantityString(
                    R.plurals.review,
                    movie.numberOfRatings,
                    movie.numberOfRatings
                )
                movieDuration.text = itemView.context.getString(R.string.duration, movie.runtime)
                movieName.text = movie.title
                movieName.setShaderForGradient()
            }

            binding.root.setSafeOnClickListener { view ->
                val cX = (view.left + view.right) / 2
                val cY = (view.top + view.bottom) / 2
                listener(movie, cX, cY)
            }
        }
    }

    abstract class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(movie: Movie)
    }

    private companion object {
        private const val MAX_GENRE = 3
    }
}

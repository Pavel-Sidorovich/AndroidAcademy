package com.pavesid.androidacademy.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.movies.Movie
import com.pavesid.androidacademy.databinding.MovieItemBinding
import com.pavesid.androidacademy.utils.extensions.setSafeOnClickListener
import com.pavesid.androidacademy.utils.extensions.setShaderForGradient
import com.pavesid.androidacademy.utils.extensions.toRightUrl
import java.util.Collections

internal class MoviesAdapter(
    private val likeListener: (Movie) -> Unit,
    private val listener: (Int, Int, Int) -> Unit
) :
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
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener = listener,
            likeListener = likeListener
        )

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

    inner class MoviesViewHolder(
        private val binding: MovieItemBinding,
        private val listener: (Int, Int, Int) -> Unit,
        private val likeListener: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.apply {
                movieRectanglePg.text = itemView.context.getString(R.string.pg, movie.minimumAge)
                if (movie.poster.isNotBlank()) {
                    movieOrig.load(movie.poster.toRightUrl()) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(14f, 14f, 0f, 0f))
                    }
                } else {
                    movieOrig.load(DETAILS_PLACEHOLDER) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(14f, 14f, 0f, 0f))
                    }
                }

                movieTag.text = movie.genres.take(MAX_GENRE).joinToString { it.name }
                movieRating.rating = movie.ratings / 2
                movieReviews.text = itemView.context.resources.getQuantityString(
                    R.plurals.review,
                    movie.numberOfRatings,
                    movie.numberOfRatings
                )
                movieName.text = movie.title
                movieName.setShaderForGradient()
                movieLikeBox.apply {
                    isSelected = movie.liked
                    setOnClickListener {
                        if (movieLikeBox.isSelected) {
                            movieLikeBox.isSelected = false
                        } else {
                            movieLikeBox.isSelected = true
                            movieLikeBox.likeAnimation()
                        }
                        likeListener.invoke(movie)
                    }
                }
            }

            binding.root.setSafeOnClickListener {
                val cX = (binding.root.left + binding.root.right) / 2
                val cY = (binding.root.top + binding.root.bottom) / 2
                listener(movie.id, cX, cY)
            }
        }
    }

    private companion object {
        private const val MAX_GENRE = 2
        private const val DETAILS_PLACEHOLDER = "https://upload.wikimedia.org/wikipedia/commons/a/a1/Out_Of_Poster.jpg"
    }
}

package com.pavesid.androidacademy.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.movies.Movie
import com.pavesid.androidacademy.databinding.MovieItemBinding
import com.pavesid.androidacademy.utils.extensions.setSafeOnClickListener
import com.pavesid.androidacademy.utils.extensions.setShaderForGradient
import com.pavesid.androidacademy.utils.extensions.toW342Url
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class MoviesAdapter(
    private val likeListener: (Movie) -> Unit,
    private val listener: (String, Int, Int) -> Unit,
    private val loadMore: () -> Unit
) :
    RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    internal var movies: List<Movie> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder =
        MoviesViewHolder(
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener = listener,
            likeListener = likeListener
        )

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(movie = movies[position])
        if (position == movies.size - 15) {
            loadMore()
        }
    }

    class MoviesViewHolder(
        private val binding: MovieItemBinding,
        private val listener: (String, Int, Int) -> Unit,
        private val likeListener: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.apply {
                movieRectanglePg.text = itemView.context.getString(R.string.pg, movie.minimumAge)
                if (movie.poster.isNotBlank()) {
                    movieOrig.load(movie.poster.toW342Url()) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(14f, 14f, 0f, 0f))
                    }
                } else {
                    movieOrig.load(R.drawable.out_of_poster) {
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
                        isSelected = if (isSelected) {
                            false
                        } else {
                            likeAnimation()
                            true
                        }
                        movie.liked = isSelected
                        likeListener(
                            movie
                        )
                    }
                }
            }

            binding.root.setSafeOnClickListener {
                val cX = (binding.root.left + binding.root.right) / 2
                val cY = (binding.root.top + binding.root.bottom) / 2
                listener(Json.encodeToString(movie), cX, cY)
            }
        }
    }

    private companion object {
        private const val MAX_GENRE = 2
    }
}

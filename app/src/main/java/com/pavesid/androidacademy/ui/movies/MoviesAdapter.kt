package com.pavesid.androidacademy.ui.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.local.model.MoviePreview
import com.pavesid.androidacademy.utils.setShaderForGradient

internal class MoviesAdapter(private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<MoviePreview>() {
        override fun areItemsTheSame(oldItem: MoviePreview, newItem: MoviePreview): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: MoviePreview, newItem: MoviePreview): Boolean =
            oldItem.hashCode() == newItem.hashCode()
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var movies: List<MoviePreview>
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
        holder.bind(moviePreview = movies[position])

    class MoviesViewHolder(itemView: View, private val listener: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val name = itemView.findViewById<TextView>(R.id.movie_name)
        private val pg = itemView.findViewById<TextView>(R.id.movie_rectangle_pg)
        private val duration = itemView.findViewById<TextView>(R.id.movie_duration)
        private val tags = itemView.findViewById<TextView>(R.id.movie_tag)
        private val reviews = itemView.findViewById<TextView>(R.id.movie_reviews)
        private val origImage = itemView.findViewById<ImageView>(R.id.movie_orig)
        private val rating =
            itemView.findViewById<me.zhanghai.android.materialratingbar.MaterialRatingBar>(R.id.movie_rating)

        private var id: Int = 0

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(moviePreview: MoviePreview) {
            id = moviePreview.id
            pg.text = itemView.context.getString(R.string.pg, moviePreview.pg)
            origImage.load(moviePreview.image)
            tags.text = moviePreview.tags.joinToString()
            rating.rating = moviePreview.rating.toFloat()
            reviews.text = itemView.context.resources.getQuantityString(
                R.plurals.review,
                moviePreview.reviews,
                moviePreview.reviews
            )
            duration.text = itemView.context.getString(R.string.duration, moviePreview.duration)
            name.text = moviePreview.name
            name.setShaderForGradient()
        }

        override fun onClick(p0: View?) {
            listener(id)
        }
    }
}

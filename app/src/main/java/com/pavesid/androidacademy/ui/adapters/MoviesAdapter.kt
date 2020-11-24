package com.pavesid.androidacademy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.local.model.Movie
import com.pavesid.androidacademy.utils.Utils

class MoviesAdapter(private val listener: (String) -> Unit) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.title == newItem.title

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

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) = holder.bind(position = position)

    class MoviesViewHolder(itemView: View, private val listener: (String) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val name = itemView.findViewById<TextView>(R.id.movie_name)

        private var title: String? = null

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(position: Int) {
            title = position.toString()

            name.paint.shader = Utils.getShaderForGradientTextView(name)
        }

        override fun onClick(p0: View?) {
            title?.let(listener)
        }
    }
}

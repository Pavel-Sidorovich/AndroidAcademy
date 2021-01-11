package com.pavesid.androidacademy.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.databinding.GenreItemBinding

class GenresAdapter(private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<GenresAdapter.GenresViewHolder>() {

    private var genres: List<Genre> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder =
        GenresViewHolder(
            GenreItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) =
        holder.bind(genre = genres[position])

    override fun getItemCount(): Int = genres.size

    fun setData(data: List<Genre>) {
        genres = data
        notifyDataSetChanged()
    }

    inner class GenresViewHolder(
        private val binding: GenreItemBinding,
        private val listener: (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: Genre) {
            binding.root.apply {
                isSelected = genre.isChecked
                text = genre.name
                setOnClickListener {
                    genres.forEach {
                        it.isChecked = false
                    }
                    genre.isChecked = true
                    notifyDataSetChanged()
                    listener(genre.id)
                }
            }
        }
    }
}

package com.pavesid.androidacademy.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.databinding.GenreItemBinding

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class GenresAdapter(private val listener: (Long) -> Unit) :
    RecyclerView.Adapter<GenresAdapter.ViewHolder>() {

    private var genres: List<GenreDataItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ITEM -> GenresViewHolder(
                GenreItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
            else -> HeaderViewHolder(
                GenreItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (genres[position]) {
            is GenreDataItem.HeaderItem -> ITEM_VIEW_TYPE_HEADER
            else -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(genre = genres[position])

    override fun getItemCount(): Int = genres.size

    fun setData(data: List<Genre>) {
        genres = listOf(GenreDataItem.HeaderItem) + data.map { GenreDataItem.GenreItem(it) }
        notifyDataSetChanged()
    }

    inner class GenresViewHolder(
        private val binding: GenreItemBinding,
        private val listener: (Long) -> Unit
    ) : ViewHolder(binding) {

        override fun bind(genre: GenreDataItem) {
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

    inner class HeaderViewHolder(
        private val binding: GenreItemBinding,
        private val listener: (Long) -> Unit
    ) : ViewHolder(binding) {

        override fun bind(genre: GenreDataItem) {
            binding.root.apply {
                isSelected = genre.isChecked
                text = this.context.getText(R.string.all)
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

    abstract class ViewHolder(
        binding: GenreItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(genre: GenreDataItem)
    }
}

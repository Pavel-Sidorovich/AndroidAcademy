package com.pavesid.androidacademy.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.pavesid.androidacademy.data.actors.Cast
import com.pavesid.androidacademy.databinding.CastItemBinding
import com.pavesid.androidacademy.utils.extensions.toRightUrl
import javax.inject.Inject

internal class CastAdapter @Inject constructor() :
    RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    private var actors: List<Cast> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder =
        CastViewHolder(CastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) =
        holder.bind(actor = actors[position])

    override fun getItemCount(): Int = actors.size

    fun setData(data: List<Cast>) {
        actors = data
        notifyDataSetChanged()
    }

    inner class CastViewHolder(private val binding: CastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(actor: Cast) {
            binding.apply {
                nameCast.text = actor.name
                if (actor.profilePath.isNullOrEmpty()) {
                    imageCast.load(ACTOR_PLACEHOLDER) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(8f))
                    }
                } else {
                    imageCast.load(actor.profilePath.toRightUrl()) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(8f))
                    }
                }
                characterCast.text = actor.character
            }
        }
    }

    private companion object {
        private const val ACTOR_PLACEHOLDER = "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1570689994l/49418257._SX318_SY475_.jpg"
    }
}

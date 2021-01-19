package com.pavesid.androidacademy.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.actors.Cast
import com.pavesid.androidacademy.databinding.CastItemBinding
import com.pavesid.androidacademy.utils.extensions.toW342Url

internal class CastAdapter :
    RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    internal var actors: List<Cast> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder =
        CastViewHolder(CastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) =
        holder.bind(actor = actors[position])

    override fun getItemCount(): Int = actors.size

    class CastViewHolder(private val binding: CastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(actor: Cast) {
            binding.apply {
                nameCast.text = actor.name
                if (actor.profilePath.isNullOrEmpty()) {
                    imageCast.load(R.drawable.actor) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(8f))
                    }
                } else {
                    imageCast.load(actor.profilePath.toW342Url()) {
                        crossfade(true)
                        placeholder(R.drawable.actor)
                        transformations(RoundedCornersTransformation(8f))
                    }
                }
                characterCast.text = actor.character
            }
        }
    }
}

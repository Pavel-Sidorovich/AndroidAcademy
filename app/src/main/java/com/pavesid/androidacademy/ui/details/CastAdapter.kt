package com.pavesid.androidacademy.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.pavesid.androidacademy.data.Actor
import com.pavesid.androidacademy.databinding.CastItemBinding
import javax.inject.Inject

internal class CastAdapter @Inject constructor() :
    RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    private var actors: List<Actor> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder =
        CastViewHolder(CastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) =
        holder.bind(actor = actors[position])

    override fun getItemCount(): Int = actors.size

    fun setData(data: List<Actor>) {
        actors = data
        notifyDataSetChanged()
    }

    inner class CastViewHolder(private val binding: CastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(actor: Actor) {
            binding.apply {
                nameCast.text = actor.name
                imageCast.load(actor.picture) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation(8f))
                }
            }
        }
    }
}

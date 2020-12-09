package com.pavesid.androidacademy.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.Actor

internal class CastAdapter : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    private var actors: List<Actor> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder =
        CastViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cast_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) =
        holder.bind(actor = actors[position])

    override fun getItemCount(): Int = actors.size

    fun setData(data: List<Actor>) {
        actors = data
        notifyDataSetChanged()
    }

    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name = itemView.findViewById<TextView>(R.id.name_cast)
        private val image = itemView.findViewById<ImageView>(R.id.image_cast)

        fun bind(actor: Actor) {
            name.text = actor.name
            image.load(actor.picture) {
                crossfade(true)
                transformations(RoundedCornersTransformation(8f))
            }
        }
    }
}

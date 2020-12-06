package com.pavesid.androidacademy.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.local.model.Actor

internal class CastAdapter : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Actor>() {
        override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean =
            oldItem.hashCode() == newItem.hashCode()
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var actors: List<Actor>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder =
        CastViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cast_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) = holder.bind(actor = actors[position])

    override fun getItemCount(): Int = actors.size

    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name = itemView.findViewById<TextView>(R.id.name_cast)
        private val image = itemView.findViewById<ImageView>(R.id.image_cast)

        fun bind(actor: Actor) {
            name.text = actor.name
            image.load(actor.imageSrc) {
                transformations(RoundedCornersTransformation(8f))
            }
        }
    }
}

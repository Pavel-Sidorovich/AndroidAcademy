package com.pavesid.androidacademy.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.pavesid.androidacademy.data.actors.Crew
import com.pavesid.androidacademy.databinding.CastItemBinding
import com.pavesid.androidacademy.utils.extensions.toRightUrl
import javax.inject.Inject

internal class CrewAdapter @Inject constructor() :
    RecyclerView.Adapter<CrewAdapter.CastViewHolder>() {

    private var crews: List<Crew> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder =
        CastViewHolder(CastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) =
        holder.bind(crew = crews[position])

    override fun getItemCount(): Int = crews.size

    fun setData(data: List<Crew>) {
        crews = data
        notifyDataSetChanged()
    }

    inner class CastViewHolder(private val binding: CastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(crew: Crew) {
            binding.apply {
                nameCast.text = crew.name
                if (crew.profilePath.isNullOrEmpty()) {
                    imageCast.load(ACTOR_PLACEHOLDER) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(8f))
                    }
                } else {
                    imageCast.load(crew.profilePath.toRightUrl()) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(8f))
                    }
                }
                characterCast.text = crew.job
            }
        }
    }

    private companion object {
        private const val ACTOR_PLACEHOLDER = "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1570689994l/49418257._SX318_SY475_.jpg"
    }
}

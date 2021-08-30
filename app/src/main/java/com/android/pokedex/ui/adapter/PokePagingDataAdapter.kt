package com.android.pokedex.ui.adapter

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.pokedex.R
import com.android.pokedex.data.model.Pokemon
import com.android.pokedex.utils.Utils.getDominantColor
import com.android.pokedex.utils.Utils.getPokemonImgFromNameOrId
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_pokemon.view.*

class PokePagingDataAdapter(private val onPokemonClickListener: OnPokemonClickListener) :
    PagingDataAdapter<Pokemon, PokePagingDataAdapter.PagingViewHolder>(POKEMON_COMPARATOR) {

    interface OnPokemonClickListener {
        fun onPokemonClicked(position: Int, pokemon: Pokemon?)
    }

    class PagingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: PagingViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            bind(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PagingViewHolder(layoutInflater.inflate(R.layout.item_pokemon, parent, false))
    }

    companion object {
        private val POKEMON_COMPARATOR = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon) =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon) =
                oldItem == newItem
        }
    }

    private fun bind(holder: PagingViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onPokemonClickListener.onPokemonClicked(position, getItem(position))
        }
        val pokemonNo = getItem(position)
        holder.itemView.pokemon_id.text = position.inc().toString().prependIndent("#")
        val nameOrId = pokemonNo?.url?.split(
            "/pokemon/"
        )?.get(1)?.replace("/", "")

        Glide.with(holder.itemView.poke_img.context)
            .load(nameOrId?.getPokemonImgFromNameOrId())
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.itemView.pg.visibility = View.GONE
                    return false
                }

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.itemView.pg.visibility = View.GONE
                    val dominantColor = holder.itemView.imageView.getDominantColor(resource)
                    holder.itemView.imageView.setBackgroundColor(dominantColor)
                    return false
                }
            })
            .into(holder.itemView.poke_img)
        holder.itemView.pokemon_name.text = getItem(position)?.name
    }


}
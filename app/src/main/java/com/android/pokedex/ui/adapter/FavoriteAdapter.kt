package com.android.pokedex.ui.adapter

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.android.pokedex.R
import com.android.pokedex.data.model.cache.CachedPokemons
import com.android.pokedex.utils.Utils.getDominantColor
import com.android.pokedex.utils.Utils.getPokemonImgFromNameOrId
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_pokemon.view.*

class FavoriteAdapter(
    private var _cachedPokemonList: List<CachedPokemons>,
    private val onPokemonClick: OnPokemonClickedListener,
) :
    RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

    interface OnPokemonClickedListener {
        fun onPokemonClick(name: String?, id: String?)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(layoutInflater.inflate(R.layout.item_pokemon, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        bindUI(holder, position)
    }

    private fun bindUI(holder: MyViewHolder, position: Int) {
        holder.itemView.pokemon_id.text = position.inc().toString().prependIndent("#")
        Glide.with(holder.itemView.poke_img.context)
            .load(_cachedPokemonList[position].id.toString().getPokemonImgFromNameOrId())
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

        holder.itemView.setOnClickListener {
            onPokemonClick.onPokemonClick(
                _cachedPokemonList[position].pokemon_name,
                _cachedPokemonList[position].id.toString()
            )
        }

        holder.itemView.pokemon_name.text = _cachedPokemonList[position].pokemon_name

    }

    fun setList(CachedPokemonList: List<CachedPokemons>) {
        _cachedPokemonList = CachedPokemonList
    }

    override fun getItemCount() = _cachedPokemonList.size

}
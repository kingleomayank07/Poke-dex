package com.android.pokedex.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.pokedex.R
import com.android.pokedex.data.model.EvolutionChainList
import com.android.pokedex.utils.Utils.getPokemonImgFromNameOrId
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_evolution.view.*

class EvolutionAdapter(
    private val chain: List<EvolutionChainList>?,
    private val onPokemonClick: OnPokemonClickedListener,
    private val pokemonName: String
) :
    RecyclerView.Adapter<EvolutionAdapter.MyViewHolder>() {

    interface OnPokemonClickedListener {
        fun onPokemonClick(name: String?, url: String?)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(layoutInflater.inflate(R.layout.item_evolution, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        bind(holder, position)
    }

    override fun getItemCount(): Int = chain?.size!!

    private fun bind(holder: MyViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(getUrl(position)?.getPokemonImgFromNameOrId())
            .into(holder.itemView.poke_img)

        holder.itemView.setOnClickListener {
            if (pokemonName != chain?.get(position)?.name) {
                onPokemonClick.onPokemonClick(
                    chain?.get(position)?.name,
                    getUrl(position)
                )
            }
        }

        holder.itemView.pokemon_name.text = chain?.get(position)?.name

        if (position % 2 == 1) {
            holder.itemView.arrow.visibility = View.GONE
        }

    }

    private fun getUrl(position: Int): String? {
        return chain?.get(position)?.url?.split(
            "/pokemon-species/"
        )?.get(1)?.replace("/", "")
    }

}
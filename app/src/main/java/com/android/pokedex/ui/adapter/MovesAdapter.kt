package com.android.pokedex.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.pokedex.R
import com.android.pokedex.data.model.pokemondetailsmodels.Move
import kotlinx.android.synthetic.main.item_moves.view.*

const val star = "*"

class MovesAdapter(private val moves: List<Move>?) :
    RecyclerView.Adapter<MovesAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(layoutInflater.inflate(R.layout.item_moves, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        bind(holder, position)
    }

    override fun getItemCount(): Int = moves?.size!!

    private fun bind(holder: MyViewHolder, position: Int) {
        holder.itemView.moves.text = star.plus("  ${moves?.get(position)?.move?.name}")
    }

}
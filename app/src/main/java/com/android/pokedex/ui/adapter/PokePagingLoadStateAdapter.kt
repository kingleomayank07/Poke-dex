package com.android.pokedex.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.pokedex.R
import kotlinx.android.synthetic.main.layout_progress_bar.view.*

class PokeLoadStateAdapter : LoadStateAdapter<PokeLoadStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        bind(holder, loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return LoadStateViewHolder(
            layoutInflater.inflate(
                R.layout.layout_progress_bar,
                parent,
                false
            )
        )
    }

    private fun bind(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.itemView.progress_bar.isVisible = loadState is LoadState.Loading
    }

}
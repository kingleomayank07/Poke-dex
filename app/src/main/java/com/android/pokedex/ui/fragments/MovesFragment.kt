package com.android.pokedex.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.pokedex.R
import com.android.pokedex.ui.adapter.MovesAdapter
import kotlinx.android.synthetic.main.moves_fragment.*

class MovesFragment : Fragment(R.layout.moves_fragment) {

    private val _movesFragmentArgs: MovesFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
    }

    private fun setUpRecycler() {
        moves_recycler.setHasFixedSize(true)
        moves_recycler.layoutManager = LinearLayoutManager(requireContext())
        moves_recycler.adapter = MovesAdapter(_movesFragmentArgs.pokemonDetails?.moves)
    }
}

package com.android.pokedex.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.pokedex.databinding.MovesFragmentBinding
import com.android.pokedex.ui.adapter.MovesAdapter

class MovesFragment : Fragment() {

    private val _movesFragmentArgs: MovesFragmentArgs by navArgs()
    private var _binding: MovesFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
    }

    private fun setUpRecycler() {
        binding.movesRecycler.setHasFixedSize(true)
        binding.movesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.movesRecycler.adapter = MovesAdapter(_movesFragmentArgs.pokemonDetails?.moves)
    }
}

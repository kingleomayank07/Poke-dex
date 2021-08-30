package com.android.pokedex.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.android.pokedex.App
import com.android.pokedex.R
import com.android.pokedex.data.api.ApiServiceImpl
import com.android.pokedex.data.model.Pokemon
import com.android.pokedex.data.model.cache.CachedPokemons
import com.android.pokedex.ui.adapter.FavoriteAdapter
import com.android.pokedex.ui.viewmodels.PokemonDetailViewModel
import com.android.pokedex.utils.Status
import com.android.pokedex.utils.Utils.hideKeyboard
import com.android.pokedex.utils.Utils.hideShowView
import com.android.pokedex.utils.Utils.onSearch
import com.android.pokedex.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList

class FavoriteFragment : Fragment(R.layout.fragment_favorite),
    FavoriteAdapter.OnPokemonClickedListener {

    private val _pokemonDetailViewModel: PokemonDetailViewModel by
    viewModels(factoryProducer = { ViewModelFactory(ApiServiceImpl(), App.getInstance()) })
    private var _cachedPokemon: List<CachedPokemons> = ArrayList()
    private val adapter = FavoriteAdapter(_cachedPokemon, this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFavoritePokemons()
        navigateToDetailScreenObserver()

        floating_search_view.onSearch {
            pg.hideShowView(true)
            val name = floating_search_view.text.toString().toLowerCase(Locale.ROOT)
            if (name.contains(" ")) {
                _pokemonDetailViewModel.getPokemonDetail(name.replace(" ", "-"))
            } else {
                _pokemonDetailViewModel.getPokemonDetail(name)
            }
            requireActivity().hideKeyboard()
        }
    }

    private fun navigateToDetailScreenObserver() {
        _pokemonDetailViewModel.pokemonDetailObserver().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        "oops! ${floating_search_view.text} is not a correct pokemon name",
                        Toast.LENGTH_SHORT
                    ).show()
                    pg.hideShowView(false)
                }
                Status.LOADING -> {
                    pg.hideShowView(true)
                }
                Status.SUCCESS -> {
                    floating_search_view.setText("")
                    pg.hideShowView(false)
                    if (!it?.data?.name.isNullOrEmpty()) {
                        findNavController().navigate(
                            R.id.pokemonProfileDetail,
                            Bundle().apply {
                                this.putParcelable("pokemon", null)
                                this.putString("pokemon_name", it?.data?.name)
                            }
                        )
                        it.data = null
                    }
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Unexpected error occurred!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun observeFavoritePokemons() {
        _pokemonDetailViewModel.getCachedPokemonList()?.observe(viewLifecycleOwner, {
            _cachedPokemon = it
            adapter.setList(_cachedPokemon)
            pokemon_recycler.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            pokemon_recycler.adapter = adapter
        })
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.statusBarColor = resources.getColor(
            R.color.grey,
            requireContext().theme
        )
    }

    override fun onPokemonClick(name: String?, id: String?) {
        findNavController().navigate(R.id.pokemonProfileDetail, Bundle().apply {
            this.putParcelable("pokemon", Pokemon(name, id))
            this.putBoolean("is_from_evolution", true)
            this.putString("pokemon_name", null)
        })
    }
}
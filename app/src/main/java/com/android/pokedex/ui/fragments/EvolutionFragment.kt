package com.android.pokedex.ui.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.pokedex.R
import com.android.pokedex.data.api.ApiServiceImpl
import com.android.pokedex.data.model.EvolutionChainList
import com.android.pokedex.data.model.Pokemon
import com.android.pokedex.data.model.evolution.EvolutionResponse
import com.android.pokedex.ui.adapter.EvolutionAdapter
import com.android.pokedex.ui.viewmodels.EvolutionViewModel
import com.android.pokedex.utils.Status
import com.android.pokedex.utils.Utils.hideShowView
import com.android.pokedex.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_evolution.*
import kotlinx.android.synthetic.main.layout_full_pg.view.*
import java.util.*
import kotlin.collections.ArrayList

class EvolutionFragment : Fragment(R.layout.fragment_evolution),
    EvolutionAdapter.OnPokemonClickedListener {

    private val _EvolutionFragmentArgs: EvolutionFragmentArgs by navArgs()
    private val _EvolutionViewModel: EvolutionViewModel by
    viewModels(factoryProducer = { ViewModelFactory(ApiServiceImpl(), null) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSpecies(_EvolutionFragmentArgs.pokemonId)
        progress_bar.progress_icon.indeterminateDrawable.setColorFilter(
            _EvolutionFragmentArgs.color,
            PorterDuff.Mode.MULTIPLY
        )
    }

    private fun getSpecies(id: Int) {
        _EvolutionViewModel.getSpecies(id)
        observeSpecies()
    }

    private fun observeSpecies() {
        _EvolutionViewModel.observeSpecies().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    getEvolutions(
                        it.data?.evolutionChain?.url
                            ?.split("evolution-chain/")
                            ?.get(1)?.replace("/", "")
                            ?.toInt()
                    )
                }
                Status.LOADING -> {
                    Log.d("TAG", "observeSpecies: ${it.data}")
                }
                Status.ERROR -> {
                    Log.d("TAG", "observeSpecies: ${it.data}")
                }
                else -> {
                    Log.d("TAG", "observeSpecies: ${it.data}")
                }
            }
        })
    }

    private fun getEvolutions(id: Int?) {
        if (id != null) {
            _EvolutionViewModel.getEvolutions(id)
        } else {
            showEmptyMessage()
        }
        observeEvolution()
    }

    private fun observeEvolution() {
        _EvolutionViewModel.observeEvolution()
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        setUpEvolutionRecycler(it.data)
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        showEmptyMessage()
                    }
                    else -> {
                        showEmptyMessage()
                    }
                }
            })
    }

    private fun setUpEvolutionRecycler(data: EvolutionResponse?) {
        if (data == null) {
            showEmptyMessage()
            return
        }
        val evolutionChainList = ArrayList<EvolutionChainList>()
        try {
            evolutionChainList.add(
                EvolutionChainList(
                    data.chain?.species?.url,
                    data.chain?.species?.name
                )
            )
            evolutionChainList.add(
                EvolutionChainList(
                    data.chain?.evolves_to?.get(0)?.species?.url,
                    data.chain?.evolves_to?.get(0)?.species?.name
                )
            )
            evolutionChainList.add(
                EvolutionChainList(
                    data.chain?.evolves_to?.get(0)?.evolves_to
                        ?.get(0)?.species?.url,
                    data.chain?.evolves_to?.get(0)?.evolves_to
                        ?.get(0)?.species?.name
                )
            )
            if (evolutionChainList.size >= 2) {
                evolutionChainList.clear()
                evolutionChainList.add(
                    EvolutionChainList(
                        data.chain?.species?.url,
                        data.chain?.species?.name
                    )
                )
                evolutionChainList.add(
                    EvolutionChainList(
                        data.chain?.evolves_to?.get(0)?.species?.url,
                        data.chain?.evolves_to?.get(0)?.species?.name
                    )
                )
                evolutionChainList.add(
                    EvolutionChainList(
                        data.chain?.evolves_to?.get(0)?.species?.url,
                        data.chain?.evolves_to?.get(0)?.species?.name
                    )
                )
                evolutionChainList.add(
                    EvolutionChainList(
                        data.chain?.evolves_to?.get(0)?.evolves_to
                            ?.get(0)?.species?.url,
                        data.chain?.evolves_to?.get(0)?.evolves_to
                            ?.get(0)?.species?.name
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (evolutionChainList.size == 1) {
            showEmptyMessage()
        } else {
            evolution_recycler.adapter =
                EvolutionAdapter(
                    chain = evolutionChainList,
                    onPokemonClick = this,
                    pokemonName = _EvolutionFragmentArgs.pokemonName
                )
        }
        progress_bar.hideShowView(false)
    }

    private fun showEmptyMessage() {
        empty_message.hideShowView(true)
        empty_message.text =
            _EvolutionFragmentArgs.pokemonName.plus(" ${getString(R.string.no_data_exists)}")
                .capitalize(Locale.ROOT)
    }

    override fun onPokemonClick(name: String?, url: String?) {
        findNavController().navigate(R.id.pokemonProfileDetail, Bundle().apply {
            this.putParcelable("pokemon", Pokemon(name, url))
            this.putBoolean("is_from_evolution", true)
            this.putString("pokemon_name", null)
        })
    }

}
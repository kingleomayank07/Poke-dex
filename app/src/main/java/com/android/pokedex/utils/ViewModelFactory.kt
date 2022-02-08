package com.android.pokedex.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.pokedex.data.api.ApiService
import com.android.pokedex.data.model.repository.EvolutionRepository
import com.android.pokedex.data.model.repository.HomeRepository
import com.android.pokedex.data.model.repository.PokemonDetailRepository
import com.android.pokedex.ui.viewmodels.EvolutionViewModel
import com.android.pokedex.ui.viewmodels.HomeViewModel
import com.android.pokedex.ui.viewmodels.PokemonDetailViewModel

class ViewModelFactory(
    private val apiHelper: ApiService,
    private val app: Application?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(HomeRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(PokemonDetailViewModel::class.java) -> {
                PokemonDetailViewModel(PokemonDetailRepository(apiHelper, app!!)) as T
            }
            modelClass.isAssignableFrom(EvolutionViewModel::class.java) -> {
                EvolutionViewModel(EvolutionRepository(apiHelper)) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown class name")
            }
        }
    }
}
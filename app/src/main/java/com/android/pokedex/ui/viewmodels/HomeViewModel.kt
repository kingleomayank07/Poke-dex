package com.android.pokedex.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.android.pokedex.data.model.pokemondetailsmodels.PokemonDetails
import com.android.pokedex.data.model.repository.HomeRepository
import com.android.pokedex.utils.Resource
import kotlinx.coroutines.launch

private const val CURRENT_LIMIT = 20

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    fun getAllPokemons() = repository.getAllPokemon(CURRENT_LIMIT).cachedIn(viewModelScope)

    private val mPokemonDetails = MutableLiveData<Resource<PokemonDetails?>>()

    fun getPokemonDetail(name: String?) {
        mPokemonDetails.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                val response = repository.getPokemonDetail(name)
                mPokemonDetails.postValue(Resource.success(response))
            } catch (e: Exception) {
                mPokemonDetails.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun pokemonDetailObserver() = mPokemonDetails

}
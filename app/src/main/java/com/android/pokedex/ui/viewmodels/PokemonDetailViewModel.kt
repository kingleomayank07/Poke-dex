package com.android.pokedex.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.pokedex.data.model.cache.CachedPokemons
import com.android.pokedex.data.model.pokemondetailsmodels.PokemonDetails
import com.android.pokedex.data.model.repository.PokemonDetailRepository
import com.android.pokedex.utils.Resource
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val repository: PokemonDetailRepository
) : ViewModel() {

    private val mPokemonDetails = MutableLiveData<Resource<PokemonDetails?>>()
    private var mCachedPokemons: LiveData<List<CachedPokemons>>? = MutableLiveData()

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

    fun getCachedPokemonList(): LiveData<List<CachedPokemons>>? {
        viewModelScope.launch {
            val cachedResponse = repository.getAllPokemons()
            mCachedPokemons = cachedResponse
        }
        return mCachedPokemons
    }

    fun pokemonDetailObserver() = mPokemonDetails

    suspend fun insert(cachedPokemons: CachedPokemons) = repository.insert(cachedPokemons)

    suspend fun getPokemon(name: String?) = repository.getPokemon(name)

    suspend fun update(cachedPokemons: CachedPokemons) = repository.update(cachedPokemons)

    suspend fun delete(cachedPokemons: CachedPokemons) = repository.delete(cachedPokemons)

    suspend fun deleteAllPokemons() = repository.deleteAllPokemons()


}
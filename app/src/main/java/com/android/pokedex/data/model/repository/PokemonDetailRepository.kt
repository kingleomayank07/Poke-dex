package com.android.pokedex.data.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.android.pokedex.data.api.ApiService
import com.android.pokedex.data.model.cache.CachedPokemonDao
import com.android.pokedex.data.model.cache.CachedPokemons
import com.android.pokedex.data.model.cache.PokemonDatabase
import com.android.pokedex.data.model.pokemondetailsmodels.PokemonDetails

class PokemonDetailRepository(
    private val pokeApi: ApiService,
    application: Application
) {
    suspend fun getPokemonDetail(name: String?): PokemonDetails? = pokeApi.getPokemonDetails(name)

    private var cachedPokemonDao: CachedPokemonDao? = null
    private var mFavPokemons: LiveData<List<CachedPokemons>>? = null

    init {
        val database = PokemonDatabase.getInstance(application)
        cachedPokemonDao = database.cachedPokemon
        mFavPokemons = cachedPokemonDao!!.getAllPokemons()
    }

    suspend fun insert(cachedPokemons: CachedPokemons) = cachedPokemonDao?.insert(cachedPokemons)

    suspend fun getPokemon(name: String?) = cachedPokemonDao?.getPokemon(name)

    suspend fun update(cachedPokemons: CachedPokemons) = cachedPokemonDao?.update(cachedPokemons)

    suspend fun delete(cachedPokemons: CachedPokemons) = cachedPokemonDao?.delete(cachedPokemons)

    suspend fun deleteAllPokemons() = cachedPokemonDao?.deleteAllPokemons()

    suspend fun getAllPokemons() = mFavPokemons


}
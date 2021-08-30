package com.android.pokedex.data.api

import com.android.pokedex.data.model.EvolutionChain
import com.android.pokedex.data.model.Pokemons
import com.android.pokedex.data.model.evolution.EvolutionResponse
import com.android.pokedex.data.model.pokemondetailsmodels.PokemonDetails

interface ApiService {

    suspend fun getPokemons(limit: Int, offset: Int): Pokemons

    suspend fun getPokemonDetails(name: String?): PokemonDetails?

    suspend fun getSpecies(id: Int): EvolutionChain?

    suspend fun getEvolutions(id: Int): EvolutionResponse?

}
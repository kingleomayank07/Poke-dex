package com.android.pokedex.data.api

import com.android.pokedex.data.api.RetrofitClient.mPokemonClient
import com.android.pokedex.data.model.EvolutionChain
import com.android.pokedex.data.model.Pokemons
import com.android.pokedex.data.model.evolution.EvolutionResponse
import com.android.pokedex.data.model.pokemondetailsmodels.PokemonDetails

class ApiServiceImpl : ApiService {

    override suspend fun getPokemons(limit: Int, offset: Int): Pokemons {
        return mPokemonClient.getAllPokemons(
            limit, offset
        )
    }

    override suspend fun getPokemonDetails(name: String?): PokemonDetails? {
        return mPokemonClient.getPokemonDetails(name)
    }

    override suspend fun getSpecies(id: Int): EvolutionChain? {
        return mPokemonClient.getSpecies(id)
    }

    override suspend fun getEvolutions(id: Int): EvolutionResponse? {
        return mPokemonClient.getEvolution(id)
    }
}
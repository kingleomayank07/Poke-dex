package com.android.pokedex.data.model.repository

import com.android.pokedex.data.api.ApiService
import com.android.pokedex.data.model.EvolutionChain
import com.android.pokedex.data.model.evolution.EvolutionResponse

class EvolutionRepository(private val pokeApi: ApiService) {

    suspend fun getSpecies(id: Int): EvolutionChain? {
        return pokeApi.getSpecies(id)
    }

    suspend fun getEvolution(id: Int): EvolutionResponse? {
        return pokeApi.getEvolutions(id)
    }

}
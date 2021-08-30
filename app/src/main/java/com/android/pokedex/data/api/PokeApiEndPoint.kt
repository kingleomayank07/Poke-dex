package com.android.pokedex.data.api

import com.android.pokedex.data.model.EvolutionChain
import com.android.pokedex.data.model.Pokemons
import com.android.pokedex.data.model.evolution.EvolutionResponse
import com.android.pokedex.data.model.pokemondetailsmodels.PokemonDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiEndPoint {

    @GET("pokemon")
    suspend fun getAllPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Pokemons

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(
        @Path("name") name: String?
    ): PokemonDetails?

    @GET("pokemon-species/{id}/")
    suspend fun getSpecies(
        @Path("id") id: Int
    ): EvolutionChain?

    @GET("evolution-chain/{id}/")
    suspend fun getEvolution(@Path("id") id: Int): EvolutionResponse?

}

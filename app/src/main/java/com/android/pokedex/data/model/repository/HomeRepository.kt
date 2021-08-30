package com.android.pokedex.data.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.android.pokedex.data.api.ApiService
import com.android.pokedex.data.model.PokePagingSource
import com.android.pokedex.data.model.pokemondetailsmodels.PokemonDetails

class HomeRepository(private val pokeApi: ApiService) {

    fun getAllPokemon(limit: Int) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 600,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PokePagingSource(pokeApi, limit) }
        ).liveData

    suspend fun getPokemonDetail(name: String?): PokemonDetails? = pokeApi.getPokemonDetails(name)

}
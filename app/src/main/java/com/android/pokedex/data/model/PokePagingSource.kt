package com.android.pokedex.data.model

import androidx.paging.PagingSource
import com.android.pokedex.data.api.ApiService
import retrofit2.HttpException
import java.io.IOException

class PokePagingSource(
    private val apiService: ApiService,
    private val limit: Int,
) : PagingSource<Int, Pokemon>() {
    private var offset: Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val position = params.key ?: 1
        return try {
            val response = apiService.getPokemons(limit, offset)
            offset += 20
            val pokemons = response.results
            LoadResult.Page(
                data = pokemons,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (pokemons.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

}
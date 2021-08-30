package com.android.pokedex.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL: String = "https://pokeapi.co/api/v2/"

    val mPokemonClient: PokeApiEndPoint by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PokeApiEndPoint::class.java)
    }
}
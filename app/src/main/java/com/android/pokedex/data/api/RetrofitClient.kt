package com.android.pokedex.data.api

import com.android.pokedex.data.api.network.SingleOkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL: String = "https://pokeapi.co/api/v2/"

    val mPokemonClient: PokeApiEndPoint by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(SingleOkHttpClient.getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PokeApiEndPoint::class.java)
    }
}
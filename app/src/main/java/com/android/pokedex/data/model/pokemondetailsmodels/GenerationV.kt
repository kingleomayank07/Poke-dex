package com.android.pokedex.data.model.pokemondetailsmodels

import com.google.gson.annotations.SerializedName

data class GenerationV(
    @SerializedName("black-white")
    val blackWhite: BlackWhite
)
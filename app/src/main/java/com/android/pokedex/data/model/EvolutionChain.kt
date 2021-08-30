package com.android.pokedex.data.model

import com.google.gson.annotations.SerializedName

data class EvolutionChain(
    @SerializedName("evolution_chain")
    val evolutionChain: ChainUrl?
)

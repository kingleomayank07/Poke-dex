package com.android.pokedex.data.model.evolution

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Chain(
    val evolution_details: List<EvolutionDetail>?,
    val evolves_to: List<EvolvesTo>?,
    val is_baby: Boolean?,
    val species: Species?
) : Parcelable
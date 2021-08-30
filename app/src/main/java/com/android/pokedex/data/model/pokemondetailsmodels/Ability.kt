package com.android.pokedex.data.model.pokemondetailsmodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ability(
    val ability: AbilityX,
    val is_hidden: Boolean,
    val slot: Int
) : Parcelable
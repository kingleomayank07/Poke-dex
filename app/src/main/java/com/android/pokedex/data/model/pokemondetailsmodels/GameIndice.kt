package com.android.pokedex.data.model.pokemondetailsmodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameIndice(
    val game_index: Int,
    val version: Version
) : Parcelable
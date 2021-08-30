package com.android.pokedex.data.model.pokemondetailsmodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Crystal(
    val back_default: String,
    val back_shiny: String,
    val front_default: String,
    val front_shiny: String
) : Parcelable
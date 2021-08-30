package com.android.pokedex.data.model.pokemondetailsmodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DreamWorld(
    val front_default: String,
//    val front_female: String
) : Parcelable
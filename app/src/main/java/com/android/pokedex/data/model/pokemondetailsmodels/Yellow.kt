package com.android.pokedex.data.model.pokemondetailsmodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Yellow(
    val back_default: String,
    val back_gray: String,
    val front_default: String,
    val front_gray: String
) : Parcelable
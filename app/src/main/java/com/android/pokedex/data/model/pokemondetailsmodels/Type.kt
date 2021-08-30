package com.android.pokedex.data.model.pokemondetailsmodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Type(
    val slot: Int,
    val type: TypeX
) : Parcelable
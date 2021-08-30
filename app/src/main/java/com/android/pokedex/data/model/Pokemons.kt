package com.android.pokedex.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pokemons(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>
) : Parcelable, ErrorResponse()
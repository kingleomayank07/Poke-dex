package com.android.pokedex.data.model.pokemondetailsmodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MoveLearnMethod(
    val name: String,
    val url: String
) : Parcelable
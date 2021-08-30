package com.android.pokedex.data.model.pokemondetailsmodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GenerationI(
    @SerializedName("red-blue")
    val redblue: RedBlue,
    val yellow: Yellow
) : Parcelable
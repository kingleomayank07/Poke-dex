package com.android.pokedex.data.model.pokemondetailsmodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Other(
    val dream_world: DreamWorld,
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork
) : Parcelable
package com.android.pokedex.data.model.evolution

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Trigger(
    val name: String?,
    val url: String?
) : Parcelable
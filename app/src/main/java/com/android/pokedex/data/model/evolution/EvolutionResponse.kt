package com.android.pokedex.data.model.evolution

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EvolutionResponse(
    val baby_trigger_item: String?,
    val chain: Chain?,
    val id: Int?
) : Parcelable
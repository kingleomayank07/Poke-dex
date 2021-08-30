package com.android.pokedex.data.model.evolution

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EvolutionDetail(
    val gender: String?,
    val held_item: String?,
    val item: String?,
    val known_move: String?,
    val known_move_type: String?,
    val location: String?,
    val min_affection: String?,
    val min_beauty: String?,
    val min_happiness: String?,
    val min_level: Int?,
    val needs_overworld_rain: Boolean?,
    val party_species: String?,
    val party_type: String?,
    val relative_physical_stats: String?,
    val time_of_day: String?,
    val trade_species: String?,
    val trigger: Trigger?,
    val turn_upside_down: Boolean?
) : Parcelable
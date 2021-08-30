package com.android.pokedex.data.model.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_table")
data class CachedPokemons(
    @ColumnInfo(name = "names")
    val pokemon_name: String,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int
)
package com.android.pokedex.data.model.cache

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CachedPokemonDao {

    @Insert
    suspend fun insert(cachedPokemon: CachedPokemons)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(cachedPokemon: CachedPokemons)

    @Delete
    suspend fun delete(cachedPokemon: CachedPokemons)

    @Query("SELECT * FROM pokemon_table ORDER BY id ASC")
    fun getAllPokemons(): LiveData<List<CachedPokemons>>

    @Query("DELETE FROM pokemon_table")
    suspend fun deleteAllPokemons()

    @Query("SELECT * FROM pokemon_table WHERE names In (:pokemonName)")
    suspend fun getPokemon(pokemonName: String?): CachedPokemons

}
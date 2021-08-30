package com.android.pokedex.data.model.cache

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [CachedPokemons::class], version = 1)
abstract class PokemonDatabase : RoomDatabase() {
    abstract val cachedPokemon: CachedPokemonDao

    companion object {
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        fun getInstance(context: Application): PokemonDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "pokemon_database"
                ).addCallback(callback)
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }

        private val callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }

    }
}
package com.android.pokedex.ui.viewmodels

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.pokedex.App
import com.android.pokedex.R
import com.android.pokedex.data.model.pokemondetailsmodels.PokemonDetails
import com.android.pokedex.utils.Constants
import com.android.pokedex.utils.Resource
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*

val mBiography = MutableLiveData<Resource<Document>>()

class AboutViewModel : ViewModel() {

    fun getBiography(pokemon: PokemonDetails?) {
        Biography(pokemon).execute()
    }

    fun observeBiography() = mBiography

    class Biography(private val pokemon: PokemonDetails?) :
        AsyncTask<Unit, Unit, Document>() {
        override fun doInBackground(vararg params: Unit?): Document? {
            mBiography.postValue(Resource.loading(null))
            return try {
                val name = pokemon?.name
                when {
                    name?.contains("-")!! -> {
                        val url = if (name.toUpperCase(Locale.ROOT) ==
                            App.getInstance().applicationContext
                                .getString(R.string.mime)
                        ) {
                            name.replace("-", "")
                        } else name.split("-")[0]
                        Jsoup.connect("${Constants.bioUrl}$url").get()
                    }
                    else -> {
                        Jsoup.connect("${Constants.bioUrl}$name").get()
                    }
                }
            } catch (e: Exception) {
                mBiography.postValue(Resource.error(e.message.toString(), null))
                return null
            }
        }

        override fun onPostExecute(result: Document?) {
            super.onPostExecute(result)
            mBiography.postValue(Resource.success(result))
        }
    }
}


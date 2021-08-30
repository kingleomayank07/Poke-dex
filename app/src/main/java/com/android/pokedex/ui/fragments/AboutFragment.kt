package com.android.pokedex.ui.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.android.pokedex.R
import com.android.pokedex.data.model.pokemondetailsmodels.PokemonDetails
import com.android.pokedex.ui.viewmodels.AboutViewModel
import com.android.pokedex.utils.Constants
import com.android.pokedex.utils.Status
import com.android.pokedex.utils.Utils.fromHtml
import com.android.pokedex.utils.Utils.hideShowView
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.layout_full_pg.view.*
import java.util.*


class AboutFragment : Fragment(R.layout.fragment_about) {

    private val _aboutFragmentArgs: AboutFragmentArgs by navArgs()
    private val _aboutViewModel: AboutViewModel by viewModels()
    private lateinit var ttobj: TextToSpeech
    private lateinit var mData: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUI(_aboutFragmentArgs.pokemonDetails)
        ttobj = TextToSpeech(requireContext()) {
            if (it != TextToSpeech.ERROR) {
                ttobj.language = Locale.UK
            }
        }

        ttobj.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onDone(utteranceId: String) {
                Log.d("TAG", "onDone: ${utteranceId}")
                sound.drawable.setTint(resources.getColor(R.color.black))
            }

            override fun onError(utteranceId: String) {}
            override fun onStart(utteranceId: String) {
                Log.d("TAG", "onDone: ${utteranceId}")
                sound.drawable.setTint(resources.getColor(R.color.colorSecondary))
            }
        })

        _aboutViewModel.getBiography(_aboutFragmentArgs.pokemonDetails)
        progress_bar.progress_icon.indeterminateDrawable.setColorFilter(
            _aboutFragmentArgs.color,
            PorterDuff.Mode.MULTIPLY
        )
        setBio()
    }

    override fun onResume() {
        super.onResume()
        sound.setOnClickListener {
            ttobj.speak(mData, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ttobj.stop()
        ttobj.shutdown()
    }

    private fun setBio() {
        _aboutViewModel.observeBiography().observe(viewLifecycleOwner, Observer { data ->
            when (data.status) {
                Status.SUCCESS -> {
                    mData = data.data?.html()
                        ?.split(Constants.getClass)?.get(1)
                        ?.split(Constants.getStartParagraph)?.get(1)
                        ?.split(Constants.getEndParagraph)?.get(0)
                        ?.fromHtml()
                        .toString()

                    bio.text = mData
                    group_divider.hideShowView(true)
                    progress_bar.hideShowView(false)
                }
                Status.LOADING -> {
                    group_divider.hideShowView(false)
                    progress_bar.hideShowView(true)
                }
                Status.ERROR -> {
                    group_divider.hideShowView(true)
                    progress_bar.hideShowView(false)
                }
                else -> {
                    group_divider.hideShowView(true)
                    progress_bar.hideShowView(false)
                }
            }
        })
    }

    private fun setUI(pokemon: PokemonDetails?) {
        pokemon_weight.text = pokemon?.weight?.div(10).toString().plus("kg")
        pokemon_height.text = pokemon?.height?.toFloat()?.div(10).toString().plus("m")
    }

}

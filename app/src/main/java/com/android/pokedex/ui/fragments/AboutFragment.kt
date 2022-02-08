package com.android.pokedex.ui.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.android.pokedex.R
import com.android.pokedex.data.model.pokemondetailsmodels.PokemonDetails
import com.android.pokedex.databinding.FragmentAboutBinding
import com.android.pokedex.ui.viewmodels.AboutViewModel
import com.android.pokedex.utils.Constants
import com.android.pokedex.utils.Status
import com.android.pokedex.utils.Utils.fromHtml
import com.android.pokedex.utils.Utils.hideShowView
import java.util.*

class AboutFragment : Fragment() {

    private val _aboutFragmentArgs: AboutFragmentArgs by navArgs()
    private val _aboutViewModel: AboutViewModel by viewModels()
    private lateinit var ttobj: TextToSpeech
    private lateinit var mData: String
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

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
                binding.sound.drawable.setTint(resources.getColor(R.color.black))
            }

            override fun onError(utteranceId: String) {}
            override fun onStart(utteranceId: String) {
                Log.d("TAG", "onDone: ${utteranceId}")
                binding.sound.drawable.setTint(resources.getColor(R.color.colorSecondary))
            }
        })

        _aboutViewModel.getBiography(_aboutFragmentArgs.pokemonDetails)
        binding.progressBar.progressIcon.indeterminateDrawable.setColorFilter(
            _aboutFragmentArgs.color,
            PorterDuff.Mode.MULTIPLY
        )
        setBio()
    }

    override fun onResume() {
        super.onResume()
        binding.sound.setOnClickListener {
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

                    binding.bio.text = mData
                    binding.groupDivider.hideShowView(true)
                    binding.progressBar.root.hideShowView(false)
                }
                Status.LOADING -> {
                    binding.groupDivider.hideShowView(false)
                    binding.progressBar.root.hideShowView(true)
                }
                Status.ERROR -> {
                    binding.groupDivider.hideShowView(true)
                    binding.progressBar.root.hideShowView(false)
                }
                else -> {
                    binding.groupDivider.hideShowView(true)
                    binding.progressBar.root.hideShowView(false)
                }
            }
        })
    }

    private fun setUI(pokemon: PokemonDetails?) {
        binding.pokemonWeight.text = pokemon?.weight?.div(10).toString().plus("kg")
        binding.pokemonHeight.text = pokemon?.height?.toFloat()?.div(10).toString().plus("m")
    }

}

package com.android.pokedex.ui.fragments

import android.graphics.PorterDuff.Mode
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.android.pokedex.App
import com.android.pokedex.R
import com.android.pokedex.data.api.ApiServiceImpl
import com.android.pokedex.data.model.Pokemon
import com.android.pokedex.data.model.cache.CachedPokemons
import com.android.pokedex.data.model.pokemondetailsmodels.PokemonDetails
import com.android.pokedex.data.model.pokemondetailsmodels.Type
import com.android.pokedex.ui.adapter.ViewPagerAdapter
import com.android.pokedex.ui.viewmodels.PokemonDetailViewModel
import com.android.pokedex.utils.Constants.TAB_LIST
import com.android.pokedex.utils.Coroutines
import com.android.pokedex.utils.Status
import com.android.pokedex.utils.Utils.getPokemonImgFromNameOrId
import com.android.pokedex.utils.Utils.hideShowView
import com.android.pokedex.utils.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_pokemon_profile_detail.*
import kotlinx.android.synthetic.main.layout_full_pg.view.*
import kotlinx.android.synthetic.main.layout_toolbar_home.view.*
import java.util.*
import kotlin.collections.ArrayList


class PokemonDetailFragment : Fragment(R.layout.fragment_pokemon_profile_detail),
    View.OnClickListener {

    //region variables
    private val mTAG = PokemonDetailFragment::class.java.simpleName
    private val _args: PokemonDetailFragmentArgs by navArgs()
    private val _types = ArrayList<Type>()
    private val _pokemonDetailViewModel: PokemonDetailViewModel by
    viewModels(factoryProducer = { ViewModelFactory(ApiServiceImpl(), app = App.getInstance()) })
    private var _isFromEvolution = false
    private var _isFav = false
    private var _colorInt = 0
    //endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pokemon = _args.pokemon
        if (!pokemon?.name.isNullOrEmpty())
            checkIfCached(pokemon?.name)
        val pokemonName = _args.pokemonName
        if (!pokemonName.isNullOrEmpty()) {
            checkIfCached(pokemonName)
        }
        _isFromEvolution = _args.isFromEvolution

        if (pokemonName == null)
            setUI(pokemon)
        if (pokemonName == null)
            getPokemonDetails(pokemon?.name)
        else
            getPokemonDetails(pokemonName)
    }

    private fun checkIfCached(name: String?) {
        Coroutines.io {
            val cachedPokemon = _pokemonDetailViewModel.getPokemon(name)
            if (cachedPokemon?.id != null) {
                fav.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorSecondary
                    ), Mode.MULTIPLY
                )
                _isFav = true
            }
        }
    }

    private fun getPokemonDetails(name: String?) {
        _pokemonDetailViewModel.getPokemonDetail(name)
        observePokemonDetails()
    }

    private fun observePokemonDetails() {
        _pokemonDetailViewModel.pokemonDetailObserver().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d(mTAG, "observePokemonDetails: ${it.data}")
                    it.data?.types?.forEach { type ->
                        if (!_types.contains(type)) {
                            _types.add(type)
                            setBubble(_types)
                        }
                    }
                    if (_args.pokemonName != null) {
                        setUIByName(it.data)
                    }

                    if (_args.pokemonName == null) {
                        setUpTabLayout(it.data)
                    }
                    container.hideShowView(true)
                    progress_bar.hideShowView(false)
                }
                Status.LOADING -> {
                    Log.d(mTAG, "observePokemonDetails: ${it.data}")
                }
                Status.ERROR -> {
                    progress_bar.hideShowView(false)
                    Log.d(mTAG, "observePokemonDetails: ${it.data}")
                }
                else -> {
                    progress_bar.hideShowView(false)
                    Log.d(mTAG, "observePokemonDetails: ${it.data}")
                }
            }
        })
    }

    private fun setBubble(types: ArrayList<Type>) {
        val cardView = CardView(requireContext())
        val textView = TextView(requireContext())
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
        cardView.setCardBackgroundColor(
            resources.getColor(
                R.color.transparent_white,
                requireContext().theme
            )
        )
        cardView.radius = 38F
        params.setMargins(12, 0, 16, 0)
        cardView.layoutParams = params
        textView.textSize = 16F
        textView.setPadding(30, 20, 30, 20)
        textView.setTextColor(resources.getColor(R.color.white, requireContext().theme))
        textView.width = 270
        textView.gravity = Gravity.CENTER
        textView.setTypeface(null, Typeface.BOLD)
        for (type in types) {
            textView.text = type.type.name.capitalize(Locale.ROOT)
        }
        cardView.addView(textView)
        type.addView(cardView)
    }

    private fun setUI(pokemon: Pokemon?) {
        toolbar.back.setOnClickListener(this)
        fav.setOnClickListener(this)
        pokemon_name.text = pokemon?.name
        val url = if (!_isFromEvolution) {
            pokemon?.url?.split(
                "/pokemon/"
            )?.get(1)?.replace("/", "")
        } else {
            pokemon?.url
        }
        Glide.with(this)
            .load(url?.getPokemonImgFromNameOrId())
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    val palette = Palette.Builder(resource?.toBitmap()!!).generate()
                    _colorInt = palette.getMutedColor(
                        palette.getVibrantColor(
                            requireContext().getColor(
                                R.color.colorSecondary
                            )
                        )
                    )
                    progress_bar.progress_icon.indeterminateDrawable.setColorFilter(
                        _colorInt,
                        Mode.MULTIPLY
                    )
                    imageView3.drawable.setTint(_colorInt)
                    requireActivity().window.statusBarColor = _colorInt
                    tab_layout.setSelectedTabIndicatorColor(_colorInt)
                    tab_layout.setTabTextColors(
                        resources.getColor(
                            R.color.grey,
                            requireContext().theme
                        ), _colorInt
                    )
                    return false
                }
            })
            .into(poke_img)

    }

    private fun setUIByName(pokemon: PokemonDetails?) {
        toolbar.back.setOnClickListener(this)
        fav.setOnClickListener(this)
        pokemon_name.text = pokemon?.name
        Glide.with(this)
            .load(pokemon?.id.toString().getPokemonImgFromNameOrId())
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    val palette = Palette.Builder(resource?.toBitmap()!!).generate()
                    _colorInt = palette.getMutedColor(
                        palette.getVibrantColor(
                            requireContext().getColor(
                                R.color.colorSecondary
                            )
                        )
                    )
                    progress_bar.progress_icon.indeterminateDrawable.setColorFilter(
                        _colorInt,
                        Mode.MULTIPLY
                    )
                    imageView3.drawable.setTint(_colorInt)
                    requireActivity().window.statusBarColor = _colorInt
                    tab_layout.setSelectedTabIndicatorColor(_colorInt)
                    tab_layout.setTabTextColors(
                        resources.getColor(
                            R.color.grey,
                            requireContext().theme
                        ), _colorInt
                    )
                    setUpTabLayout(pokemon)
                    return false
                }
            })
            .into(poke_img)

    }

    private fun setUpTabLayout(pokemonDetails: PokemonDetails?) {
        val pagerAdapter = ViewPagerAdapter(
            this,
            TAB_LIST.count(),
            pokemonDetails,
            pokemonDetails?.id,
            _colorInt
        )

        pagerAdapter.addFragment(AboutFragment())
        pagerAdapter.addFragment(StatsFragment())
        pagerAdapter.addFragment(EvolutionFragment())
        pagerAdapter.addFragment(MovesFragment())

        pager.adapter = pagerAdapter

        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = TAB_LIST[position]
        }.attach()

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val textView = tab?.customView as TextView?
                textView?.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val textView = tab?.customView as TextView?
                textView?.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back -> {
                findNavController().navigateUp()
            }
            R.id.fav -> {
                setFavUI()
            }
        }
    }

    private fun setFavUI() = if (_isFav) {
        val name =
            if (!_args.pokemon?.name.isNullOrEmpty()) _args.pokemon?.name else _args.pokemonName
        _isFav = false
        fav.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white), Mode.MULTIPLY
        )
        Coroutines.io {
            val id = _pokemonDetailViewModel.pokemonDetailObserver().value?.data?.id!!
            _pokemonDetailViewModel.delete(CachedPokemons(name!!, id))
        }
        Toast.makeText(
            requireContext(),
            """You have released ${name?.capitalize(Locale.ROOT)}, he will no longer be available in your favourite list""",
            Toast.LENGTH_LONG
        ).show()
    } else {
        val name =
            if (!_args.pokemon?.name.isNullOrEmpty()) _args.pokemon?.name else _args.pokemonName
        _isFav = true
        fav.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorSecondary
            ), Mode.MULTIPLY
        )
        Coroutines.io {
            val id = _pokemonDetailViewModel
                .pokemonDetailObserver()
                .value?.data?.id!!
            _pokemonDetailViewModel.insert(CachedPokemons(name!!, id))
        }
        Toast.makeText(
            requireContext(),
            "Gotcha! you caught ${name?.capitalize(Locale.ROOT)}, He will be available in favorite section",
            Toast.LENGTH_LONG
        ).show()
    }

}

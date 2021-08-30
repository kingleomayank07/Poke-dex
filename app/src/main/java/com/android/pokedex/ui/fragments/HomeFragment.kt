package com.android.pokedex.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.android.pokedex.R
import com.android.pokedex.data.api.ApiServiceImpl
import com.android.pokedex.data.model.Pokemon
import com.android.pokedex.ui.adapter.PokeLoadStateAdapter
import com.android.pokedex.ui.adapter.PokePagingDataAdapter
import com.android.pokedex.ui.viewmodels.HomeViewModel
import com.android.pokedex.utils.Status
import com.android.pokedex.utils.Utils.hideKeyboard
import com.android.pokedex.utils.Utils.hideShowView
import com.android.pokedex.utils.Utils.onSearch
import com.android.pokedex.utils.ViewModelFactory
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home), View.OnClickListener {

    private var mRewardedAd: RewardedAd? = null
    private var mTAG = HomeFragment::class.simpleName
    private var count = 0

    private val _homeViewModel: HomeViewModel
            by viewModels(factoryProducer = { ViewModelFactory(ApiServiceImpl(), null) })

    private val mAdapter =
        PokePagingDataAdapter(object : PokePagingDataAdapter.OnPokemonClickListener {
            override fun onPokemonClicked(position: Int, pokemon: Pokemon?) {
                count++
                if (count >= 5) {
                    if (mRewardedAd != null) {
                        mRewardedAd?.show(requireActivity()) {
                            onUserEarnedReward(it)
                        }
                    } else {
                        Log.d("TAG", "The rewarded ad wasn't ready yet.")
                    }
                }
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToPokemonProfileDetail(
                        pokemon = pokemon,
                        pokemonName = null
                    )
                )
            }
        })

    private fun onUserEarnedReward(rewardItem: RewardItem) {
        val rewardAmount = rewardItem.amount
        val rewardType = rewardItem.type
        if (rewardAmount != 0) {
            count = 0
        }
        Log.d("TAG", "User earned the reward.${rewardType}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadAds()
        fav_sec.setOnClickListener(this)
        navigateToDetailScreenObserver()

        floating_search_view.onSearch {
            pg.hideShowView(true)
            val name = floating_search_view.text.toString().toLowerCase(Locale.ROOT)
            if (name.contains(" ")) {
                _homeViewModel.getPokemonDetail(name.replace(" ", "-"))
            } else {
                _homeViewModel.getPokemonDetail(name)
            }
            requireActivity().hideKeyboard()
        }

        observePagingPokemons(mAdapter)
    }

    private fun loadAds() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            requireContext(),
            "ca-app-pub-4590617236783386/2760360639",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(mTAG, adError.message)
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Log.d(mTAG, "Ad was loaded.")
                    mRewardedAd = rewardedAd
                }
            })

        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(mTAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(mTAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(mTAG, "Ad showed fullscreen content.")
                // Called when ad is dismissed.
                // Don"t set the ad reference to null to avoid showing the ad a second time.
                mRewardedAd = null
            }
        }
    }

    private fun navigateToDetailScreenObserver() {
        _homeViewModel.pokemonDetailObserver().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        "oops! ${floating_search_view.text} is not a correct pokemon name",
                        Toast.LENGTH_SHORT
                    ).show()
                    pg.hideShowView(false)
                }
                Status.LOADING -> {
                    pg.hideShowView(true)
                }
                Status.SUCCESS -> {
                    floating_search_view.setText("")
                    pg.hideShowView(false)
                    if (!it?.data?.name.isNullOrEmpty()) {
                        findNavController().navigate(
                            R.id.pokemonProfileDetail,
                            Bundle().apply {
                                this.putParcelable("pokemon", null)
                                this.putString("pokemon_name", it?.data?.name)
                            }
                        )
                        it.data = null
                    }
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Unexpected error occurred!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.statusBarColor = resources.getColor(
            R.color.grey,
            requireContext().theme
        )
    }

    private fun observePagingPokemons(mAdapter: PokePagingDataAdapter) {
        _homeViewModel.getAllPokemons.observe(viewLifecycleOwner, {
            mAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
        setUpRecycler(mAdapter)
    }

    private fun setUpRecycler(mAdapter: PokePagingDataAdapter) {
        pokemon_recycler.setHasFixedSize(true)
        val adapter = mAdapter.withLoadStateHeaderAndFooter(
            header = PokeLoadStateAdapter(),
            footer = PokeLoadStateAdapter()
        )
        pokemon_recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        pokemon_recycler.adapter = adapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fav_sec -> {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteSection())
            }
        }
    }
}
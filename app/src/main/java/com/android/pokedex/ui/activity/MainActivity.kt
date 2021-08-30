package com.android.pokedex.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.android.pokedex.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var mPublisherInterstitialAd: PublisherInterstitialAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {
            Log.d("TAG", "onCreate: ${it}")
        }

        mPublisherInterstitialAd = PublisherInterstitialAd(this)
        mPublisherInterstitialAd?.adUnitId = "ca-app-pub-4590617236783386/7793179554"
        mPublisherInterstitialAd?.loadAd(PublisherAdRequest.Builder().build())

        setAdMobAdListener()
        showAd()

    }

    private fun showAd() {
        Handler(Looper.myLooper()!!).postDelayed({
            mPublisherInterstitialAd?.show()
        }, 10000)


        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun setAdMobAdListener() {
        mPublisherInterstitialAd?.adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
                mPublisherInterstitialAd?.loadAd(PublisherAdRequest.Builder().build())
            }
        }
    }

    override fun onBackPressed() {
        when (navigation_host_fragment.findNavController().currentDestination?.id) {
            R.id.homeFragment -> {
                finishAffinity()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

}
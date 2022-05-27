package id.saba.saba.data.controllers

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdsController(kontekstual: Context, TAGS: String, activityty: Activity) {
    private var konteks = kontekstual
    private var TAG = TAGS
    private var activity = activityty
    private var mInterstitialAd: InterstitialAd? = null
    // Show the ad if it's ready. Otherwise toast and restart the game.
    fun showInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad was dismissed.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mInterstitialAd = null
                    //loadAd()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    Log.d(TAG, "Ad failed to show.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mInterstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.")
                    // Called when ad is dismissed.
                }
            }
            mInterstitialAd?.show(activity)
        } else {
            Log.d(TAG, "Ad wasn't loaded")
            //toast("Ad wasn't loaded.")
            loadAd()
        }
    }

    fun loadAd(){
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(konteks, "ca-app-pub-7888090877482997/5642726188", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError?.message)
                    mInterstitialAd = null
                    val error = "domain: ${adError.domain}, code: ${adError.code}, " + "message: ${adError.message}"
                    //toast("onAdFailedToLoad() with error $error")
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                    //toast("onAdLoaded()")
                    showInterstitial()
                }
            }
        )
    }
}
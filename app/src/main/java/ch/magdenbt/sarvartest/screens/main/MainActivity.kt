package ch.magdenbt.sarvartest.screens.main

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import ch.magdenbt.sarvartest.common.adSize
import ch.magdenbt.sarvartest.databinding.ActivityMainBinding
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

class MainActivity : AppCompatActivity() {
    private var isDestroyed = true
    private var bannerAd: BannerAdView? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                bannerAd = loadBannerAd(adSize(binding.root))
            }
        })
    }

    override fun onResume() {
        super.onResume()
        isDestroyed = false
    }

    override fun onPause() {
        super.onPause()
        isDestroyed = true
    }

    private fun loadBannerAd(adSize: BannerAdSize): BannerAdView {
        return binding.bannerT.apply {
            setAdSize(adSize)
            setAdUnitId("demo-banner-yandex")
            setBannerAdEventListener(object : BannerAdEventListener {
                override fun onAdLoaded() {
                    if (isDestroyed) {
                        bannerAd?.destroy()
                        return
                    }
                }

                override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                }

                override fun onAdClicked() {
                }

                override fun onLeftApplication() {
                }

                override fun onReturnedToApplication() {
                }

                override fun onImpression(impressionData: ImpressionData?) {
                }
            })
            loadAd(
                AdRequest.Builder()
                    .build(),
            )
        }
    }
}

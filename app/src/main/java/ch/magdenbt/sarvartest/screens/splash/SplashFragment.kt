package ch.magdenbt.sarvartest.screens.splash

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import ch.magdenbt.sarvartest.R
import ch.magdenbt.sarvartest.SimpleDiRepository
import ch.magdenbt.sarvartest.common.Resource
import ch.magdenbt.sarvartest.common.adSize
import ch.magdenbt.sarvartest.common.viewModelCreator
import ch.magdenbt.sarvartest.databinding.FragmentSplashBinding
import ch.magdenbt.sarvartest.model.Config
import ch.magdenbt.sarvartest.screens.main.MainActivity
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

class SplashFragment : Fragment(R.layout.fragment_splash) {
    private var isDestroyed = true
    private var bannerAd: BannerAdView? = null
    private lateinit var binding: FragmentSplashBinding
    private val viewModel by viewModelCreator {
        SplashViewModel(
            SimpleDiRepository.dataSource,
            SimpleDiRepository.ioDispatcher,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                bannerAd = loadBannerAd(adSize(binding.root))
            }
        })

        viewModel.config.observe(viewLifecycleOwner) {
            setCurrentProgress(it.progress)
            showConfig(it)
        }

        viewModel.allTasksDone.observe(viewLifecycleOwner) {
            if (it) {
                launchMainScreen()
            }
        }
        return binding.root
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
        return binding.banner.apply {
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

    @SuppressLint("SetTextI18n")
    private fun setCurrentProgress(progressPercent: Int) {
        val currentProgress = binding.progressBar.progress
        val newProgress = progressPercent.coerceIn(0, 100)

        val anim = ValueAnimator.ofInt(currentProgress, newProgress)
        anim.addUpdateListener { valueAnimator ->
            val newValue = valueAnimator.animatedValue as Int
            binding.progressBar.progress = newValue
            binding.progressPercent.text = "$newValue%"
        }

        anim.duration = 200
        anim.start()
    }

    private fun showConfig(resourceConfig: Resource<Config>) {
        val msg = when (resourceConfig) {
            is Resource.Success -> "Конфигурация: ${resourceConfig.data!!.name}, версия ${resourceConfig.data.version}"
            is Resource.Error -> "Не удалось загрузить конфигурацию"
            else -> return
        }
        AlertDialog.Builder(requireContext()).setMessage(msg).create().show()
    }

    private fun launchMainScreen() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}

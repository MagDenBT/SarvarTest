package ch.magdenbt.sarvartest.common

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yandex.mobile.ads.banner.BannerAdSize
import kotlin.math.roundToInt

fun Fragment.adSize(container: View): BannerAdSize {
    var adWidthPixels = container.width
    if (adWidthPixels == 0) {
        adWidthPixels = resources.displayMetrics.widthPixels
    }
    val adWidth = (adWidthPixels / resources.displayMetrics.density).roundToInt()

    return BannerAdSize.stickySize(requireContext(), adWidth)
}

fun AppCompatActivity.adSize(container: View): BannerAdSize {
    var adWidthPixels = container.width
    if (adWidthPixels == 0) {
        adWidthPixels = resources.displayMetrics.widthPixels
    }
    val adWidth = (adWidthPixels / resources.displayMetrics.density).roundToInt()

    return BannerAdSize.stickySize(this, adWidth)
}

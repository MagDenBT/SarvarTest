package ch.magdenbt.sarvartest.screens.splash

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.magdenbt.sarvartest.R
import ch.magdenbt.sarvartest.SimpleDiRepository
import ch.magdenbt.sarvartest.common.Resource
import ch.magdenbt.sarvartest.common.viewModelCreator
import ch.magdenbt.sarvartest.databinding.FragmentSplashBinding
import ch.magdenbt.sarvartest.model.Config

class SplashFragment : Fragment(R.layout.fragment_splash){
    private lateinit var binding: FragmentSplashBinding
    private val viewModel by viewModelCreator { SplashViewModel(SimpleDiRepository.dataSource, SimpleDiRepository.ioDispatcher) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)

        viewModel.config.observe(viewLifecycleOwner){
            setCurrentProgress(it.progress)
        }
        return binding.root


    }

    private fun setCurrentProgress(progressPercent: Int) {
        val currentProgress = binding.progressBar.progress
        val newProgress = progressPercent.coerceIn(0, 100)

        val anim = ValueAnimator.ofInt(currentProgress, newProgress)
        anim.addUpdateListener { valueAnimator ->
            val newValue = valueAnimator.animatedValue as Int
            binding.progressBar.progress = newValue
            binding.progressPercent.text = newValue.toString() + "%"
        }


        anim.duration = 200

        anim.start()

    }
}

package com.elephant.spaceimage.ui.screens.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.elephant.spaceimage.R
import com.elephant.spaceimage.databinding.FragmentSplashBinding
import com.elephant.spaceimage.ui.screens.base.BaseFragment
import com.elephant.spaceimage.ui.screens.main.MainActivity
import kotlinx.coroutines.delay

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenStarted {
            renderAnimation()
            delay(3000)
            navigate()
        }
    }

    private fun renderAnimation() {
        binding.progressBar.alpha = 0f
        binding.progressBar.animate()
            .alpha(0.7f)
            .setDuration(2000)
            .start()

        binding.nameAppTv.alpha = 0f
        binding.nameAppTv.animate()
            .alpha(0.7f)
            .setDuration(2000)
            .start()

    }

    private fun navigate() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }
}
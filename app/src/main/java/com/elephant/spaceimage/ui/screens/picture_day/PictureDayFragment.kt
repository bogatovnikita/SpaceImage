package com.elephant.spaceimage.ui.screens.picture_day

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.elephant.spaceimage.R
import com.elephant.spaceimage.databinding.FragmentPictureDayBinding
import com.elephant.spaceimage.ui.screens.base.BaseFragment
import com.elephant.spaceimage.utils.viewModelCreator

class PictureDayFragment :
    BaseFragment<FragmentPictureDayBinding>(FragmentPictureDayBinding::inflate) {

    private val args by navArgs<PictureDayFragmentArgs>()
    private val viewModel by viewModelCreator { PictureDayViewModel(args.choiceDate) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObservers()
        initClickRefreshButton()
    }

    private fun initClickRefreshButton() {
        binding.refreshBtn.setOnClickListener {
            binding.progressBar.isVisible = true
            binding.refreshBtn.isVisible = false
            viewModel.getNasaApi()
        }
    }

    private fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.isLoading) binding.progressBar.isVisible = false
            if (state.isLoadingError) {
                binding.progressBar.isVisible = false
                binding.refreshBtn.isVisible = true
            }
            if (state.hdUrl.isNotEmpty() && state.isLoading) {
                initPictureForGlide(state.hdUrl)
            }
            if (state.hdUrl.isEmpty() && state.isLoading) {
                initPictureForGlide(state.url)
            }
        }
    }

    private fun initPictureForGlide(url: String) {
        Glide.with(requireActivity())
            .load(url)
            .placeholder(R.drawable.ic_no_image)
            .into(binding.pictureIv)
    }
}
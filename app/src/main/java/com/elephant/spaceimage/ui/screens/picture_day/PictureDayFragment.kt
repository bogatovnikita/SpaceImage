package com.elephant.spaceimage.ui.screens.picture_day

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import coil.load
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
        initClickButton()

    }

    private fun initClickButton() {
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
            if (state.hdUrl.isNotEmpty()) {
                binding.pictureIv.load(state.hdUrl) {
                    //TODO красивый плейсхолдер
                    placeholder(R.drawable.placeholder)
                    crossfade(2000)
                }
            } else {
                binding.pictureIv.load(state.url){


                }
            }

        }
    }
}
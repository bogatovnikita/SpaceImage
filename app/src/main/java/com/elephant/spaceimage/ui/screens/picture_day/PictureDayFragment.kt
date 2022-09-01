package com.elephant.spaceimage.ui.screens.picture_day

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.elephant.spaceimage.R
import com.elephant.spaceimage.databinding.FragmentPictureDayBinding
import com.elephant.spaceimage.ui.screens.base.BaseFragment
import com.elephant.spaceimage.utils.viewModelCreator
import java.io.ByteArrayOutputStream

class PictureDayFragment :
    BaseFragment<FragmentPictureDayBinding>(FragmentPictureDayBinding::inflate) {

    private val args by navArgs<PictureDayFragmentArgs>()
    private val viewModel by viewModelCreator { PictureDayViewModel(args.choiceDate) }
    private lateinit var pictureBitMap: Bitmap
    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(), ::onGotWriteStoragePermissionResult
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObservers()
        initClickRefreshButton()
        initClickWatchVideoButton()
    }

    private fun initToolbar() {
        binding.toolbarLayout.visibility = View.VISIBLE
        if (viewModel.state.value!!.mediaType == "video") {
            binding.toolbar.menu.findItem(R.id.screensaver).isVisible = false
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.share_picture -> {
                    shareInPicture()
                    true
                }
                R.id.screensaver -> {
                    requestCameraPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    true
                }
                else -> false
            }
        }
    }

    private fun installPictureForWallpaper() {
        val uri = getImageUri(requireContext(), pictureBitMap)
        val intent = Intent(Intent.ACTION_ATTACH_DATA)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.setDataAndType(uri, "image/jpeg")
        intent.putExtra("mimeType", "image/jpeg")
        startActivity(Intent.createChooser(intent, "Set as:"))
    }

    private fun shareInPicture() {
        val uri = viewModel.state.value!!.url
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, R.string.look_at_this_beauty)
        intent.putExtra(Intent.EXTRA_TEXT, uri)
        startActivity(
            Intent.createChooser(
                intent,
                requireContext().resources.getText(R.string.share_url)
            )
        )
    }

    private fun initClickWatchVideoButton() {
        binding.watchVideoBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.state.value!!.url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
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
            if (state.isLoadingError) {
                binding.progressBar.isVisible = false
                binding.refreshBtn.isVisible = true
            }
            if (state.isLoading && state.mediaType == "image") {
                initPictureForGlide(state.url)
            }

            if (state.mediaType == "video" && state.isLoading) {
                binding.watchVideoBtn.isVisible = true
                initToolbar()
                binding.progressBar.isVisible = false
            }
        }
    }

    private fun initPictureForGlide(url: String?) {
        Glide.with(requireActivity())
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.pictureIv.setImageBitmap(resource)
                    pictureBitMap = resource
                    binding.progressBar.isVisible = false
                    initToolbar()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun onGotWriteStoragePermissionResult(granted: Boolean) {
        if (granted) {
            installPictureForWallpaper()
        } else {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                askUserForOpeningAppSettings()
            } else {
                Toast.makeText(requireActivity(), R.string.permission_denied, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun askUserForOpeningAppSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )
        if (requireContext().packageManager.resolveActivity(
                appSettingsIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            ) == null
        ) {
            Toast.makeText(
                requireActivity(),
                R.string.permissions_denied_forever,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.permission_denied)
                .setMessage(R.string.permission_denied_forever_message)
                .setPositiveButton(R.string.open) { _, _ ->
                    startActivity(appSettingsIntent)
                }
                .create()
                .show()
        }
    }
}
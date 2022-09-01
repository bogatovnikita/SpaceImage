package com.elephant.spaceimage.ui.screens.picture_day

data class PictureDayState(
    val isLoading: Boolean = false,
    val description: String = "",
    val hdUrl: String? = "",
    val url: String = "",
    val date: String = "",
    val title: String = "",
    val mediaType: String = "",
    val isLoadingError: Boolean = false
)
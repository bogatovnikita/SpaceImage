package com.elephant.spaceimage.ui.screens.picture_day

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elephant.spaceimage.BuildConfig
import com.elephant.spaceimage.data.nasa_api.NasaApiImplementation
import com.elephant.spaceimage.data.nasa_api.NasaServerResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureDayViewModel(private val choiceDate: String) : ViewModel() {

    private val _state = MutableLiveData(PictureDayState())
    val state: LiveData<PictureDayState> = _state

    private val currentState: PictureDayState
        get() = state.value!!

    init {
        getNasaApi()
    }

    fun getNasaApi() {
        viewModelScope.launch {
            val response = NasaApiImplementation().getRetrofitImplementation()
                .getPicture(BuildConfig.API_KEY, choiceDate)
            if (response.isSuccessful && response.body() != null) {
                response.body()?.let {
                    _state.value = currentState.copy(
                        isLoading = true,
                        description = it.explanation,
                        hdUrl = it.hdurl,
                        url = it.hdurl,
                        date = it.date,
                        title = it.title,
                        mediaType = it.mediaType
                    )
                }
            } else {
                _state.value = currentState.copy(isLoadingError = true)
            }
        }
    }
}

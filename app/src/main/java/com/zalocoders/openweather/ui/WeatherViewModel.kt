package com.zalocoders.openweather.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalocoders.openweather.BuildConfig
import com.zalocoders.openweather.commons.Resource
import com.zalocoders.openweather.models.MultipleCitesWeatherResponse
import com.zalocoders.openweather.models.MyCurrentWeatherResponse
import com.zalocoders.openweather.repositories.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel @ViewModelInject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _isConverted = MutableLiveData<Boolean>()

    val isConverted: LiveData<Boolean>
        get() = _isConverted

    private val _myWeatherLiveData = MutableLiveData<Resource<MyCurrentWeatherResponse>>()

    val myWeatherLiveData: LiveData<Resource<MyCurrentWeatherResponse>>
        get() = _myWeatherLiveData

    private val _multipleWeatherLiveData = MutableLiveData<Resource<MultipleCitesWeatherResponse>>()

    val multipleWeatherLiveData: LiveData<Resource<MultipleCitesWeatherResponse>>
        get() = _multipleWeatherLiveData

    fun getCurrentWeather(lat: String, lon: String) {
        viewModelScope.launch {
            _myWeatherLiveData.value =
                repository.getCurrentWeather(lat, lon, BuildConfig.OPEN_WEATHER_API_KEY)
        }
    }

    fun getCurrentWeatherInPortuguese(lat: String, lon: String, lang: String = "pt") {
        viewModelScope.launch {
            _myWeatherLiveData.value =
                repository.getCurrentWeather(lat, lon, BuildConfig.OPEN_WEATHER_API_KEY, lang)
        }
    }

    fun getWeatherOfTenEuropeanCities(id: String) {
        viewModelScope.launch {
            _multipleWeatherLiveData.value =
                repository.getWeatherOfTenEuropeanCities(id, BuildConfig.OPEN_WEATHER_API_KEY)
        }
    }

    fun convertToPortuguese(isConverted:Boolean) {
        _isConverted.value = isConverted

    }
}
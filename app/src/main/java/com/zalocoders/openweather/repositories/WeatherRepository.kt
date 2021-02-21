package com.zalocoders.openweather.repositories

import com.zalocoders.openweather.api.services.WeatherServiceApi
import javax.inject.Inject


class WeatherRepository @Inject constructor(
    private val weatherServiceApi: WeatherServiceApi
):BaseRepository() {
    suspend fun getCurrentWeather(lat:String,lon:String,appid:String,lan:String="en") = safeApiCall {
        weatherServiceApi.getWeatherCurrentLocation(lat,lon,appid,lan)
    }
    suspend fun getWeatherOfTenEuropeanCities(id:String,appid:String,lan:String="en") = safeApiCall {
        weatherServiceApi.getWeatherMultipleLocation(id,appid,lan)
    }
}
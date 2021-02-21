package com.zalocoders.openweather.api.services

import com.zalocoders.openweather.models.MultipleCitesWeatherResponse
import com.zalocoders.openweather.models.MyCurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServiceApi {

    @GET("weather")
    suspend fun getWeatherCurrentLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String = "en",
        @Query("units") metric: String = "metric"
    ): MyCurrentWeatherResponse

    @GET("group")
    suspend fun getWeatherMultipleLocation(
        @Query("id") cityId: String,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String = "en",
        @Query("units") metric: String = "metric"
    ): MultipleCitesWeatherResponse

}
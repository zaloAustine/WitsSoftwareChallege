package com.zalocoders.openweather.models


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MultipleCitesWeatherResponse(
    @Json(name = "cnt")
    val cnt: Int,
    @Json(name = "list")
    val list: List<MultipleWeather>
):Parcelable
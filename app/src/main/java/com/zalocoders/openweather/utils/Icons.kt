package com.zalocoders.openweather.utils

import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Picasso

object Icons {

    fun setWeatherIcon(imageView: ImageView, imageId: String) {
        Picasso.get().load("https://openweathermap.org/img/wn/$imageId.png").into(imageView)
    }
}
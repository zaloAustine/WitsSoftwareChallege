package com.zalocoders.openweather.Adapters

import androidx.recyclerview.widget.DiffUtil
import com.zalocoders.openweather.models.MultipleWeather

class WeatherDiffUtilCallback : DiffUtil.ItemCallback<MultipleWeather>() {
    override fun areItemsTheSame(
        oldItem: MultipleWeather,
        newItem: MultipleWeather
    ): Boolean = oldItem === newItem

    override fun areContentsTheSame(
        oldItem: MultipleWeather,
        newItem: MultipleWeather
    ): Boolean = oldItem.id == newItem.id
}
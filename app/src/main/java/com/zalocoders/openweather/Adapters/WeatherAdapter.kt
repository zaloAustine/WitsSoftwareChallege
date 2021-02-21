package com.zalocoders.openweather.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zalocoders.openweather.databinding.WeatherItemBinding
import com.zalocoders.openweather.models.MultipleWeather


class WeatherAdapter :
    ListAdapter<MultipleWeather, WeatherAdapter.WeatherViewHolder>(Companion) {

    class WeatherViewHolder(val binding: WeatherItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val DEGREES = "\u00B0"


    companion object : DiffUtil.ItemCallback<MultipleWeather>() {
        override fun areItemsTheSame(
            oldItem: MultipleWeather,
            newItem: MultipleWeather
        ): Boolean = oldItem === newItem

        override fun areContentsTheSame(
            oldItem: MultipleWeather,
            newItem: MultipleWeather
        ): Boolean = oldItem.id == newItem.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = WeatherItemBinding.inflate(layoutInflater)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = getItem(position)
        holder.binding.cityTv.text = weather.name
        holder.binding.descriptionTv.text = weather.weather[0].description

        Picasso.get().load("https://openweathermap.org/img/wn/${weather.weather[0].icon}.png")
            .into(holder.binding.iconImageView)

        holder.binding.tempTv.text = weather.main.temp.toString() + DEGREES
    }
}
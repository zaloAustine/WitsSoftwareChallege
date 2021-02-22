package com.zalocoders.openweather.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zalocoders.openweather.databinding.WeatherItemBinding
import com.zalocoders.openweather.models.Coord
import com.zalocoders.openweather.models.MultipleWeather
import com.zalocoders.openweather.utils.Uiutils


typealias WeatherClickItem = (Coord) -> Unit

class WeatherAdapter(
    private val weatherClickListener: WeatherClickItem
) : ListAdapter<MultipleWeather, WeatherAdapter.WeatherViewHolder>(WeatherDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = WeatherItemBinding.inflate(layoutInflater)
        return WeatherViewHolder(binding, weatherClickListener)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class WeatherViewHolder(
        private val binding: WeatherItemBinding,
        val weatherClickListener: WeatherClickItem
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weather: MultipleWeather) {
            binding.weather = weather
            binding.weatherItem.setOnClickListener {
                weatherClickListener(weather.coord)
                Uiutils.showSnackBar(it, "Weather set to ${weather.name}")

            }

            binding.executePendingBindings()
        }
    }

}
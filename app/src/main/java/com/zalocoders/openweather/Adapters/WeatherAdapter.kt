package com.zalocoders.openweather.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zalocoders.openweather.databinding.WeatherItemBinding
import com.zalocoders.openweather.models.MultipleWeather
import java.text.SimpleDateFormat
import java.util.*


class WeatherAdapter :
    ListAdapter<MultipleWeather, WeatherAdapter.UserViewHolder>(Companion) {

    class UserViewHolder(val binding: WeatherItemBinding) : RecyclerView.ViewHolder(binding.root)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = WeatherItemBinding.inflate(layoutInflater)

        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val weather = getItem(position)
        holder.binding.cityTv.text = weather.name
        holder.binding.date.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
            Date()
        ).toString()

        holder.binding.tempTv.text = weather.main.temp.toString()


    }
}
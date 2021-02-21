package com.zalocoders.openweather.ui

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.zalocoders.openweather.Adapters.WeatherAdapter
import com.zalocoders.openweather.commons.Resource
import com.zalocoders.openweather.databinding.ActivityMainBinding
import com.zalocoders.openweather.models.MyCurrentWeatherResponse
import com.zalocoders.openweather.utils.Icons
import com.zalocoders.openweather.utils.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import mumayank.com.airlocationlibrary.AirLocation
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val DEGREES = "\u00B0"
    lateinit var weatherAdapter: WeatherAdapter

    @Inject
    lateinit var preferenceHelper: PreferenceHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        weatherAdapter = WeatherAdapter()
        binding.adapter = weatherAdapter

        getCitiesWeather()
        checkSwitchValue()
        airLocation.start()
        observeCurrentWeather()
    }

    private fun checkSwitchValue() {
        binding.languageSwitch.setOnCheckedChangeListener { compoundButton, boolean ->
            if (compoundButton.isChecked) {
                viewModel.convertToPortuguese(true)
            } else {
                viewModel.convertToPortuguese(false)
            }
        }
    }

    private fun getCurrentLocationWeather(lat: String, lon: String) {
        viewModel.getCurrentWeather(lat, lon)
    }

    private fun getCurrentLocationWeatherInPortuguese(lat: String, lon: String) {
        viewModel.getCurrentWeatherInPortuguese(lat, lon)
    }

    private fun observeCurrentWeather() {
        viewModel.myWeatherLiveData.observe(this, {

            when (it) {
                is Resource.Success -> {

                    populateCurrentWeather(it.value)
                    binding.progressBar.visibility = View.GONE

                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE

                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE

                }
                else -> {

                }
            }
        })
    }

    private fun getCitiesWeather() {
        viewModel.getWeatherOfTenEuropeanCities("833,2960")
        viewModel.multipleWeatherLiveData.observe(this, {
            when (it) {
                is Resource.Success -> {
                    weatherAdapter.submitList(it.value.list)
                }
                is Resource.Failure -> {
                }
                else -> {
                }
            }
        })
    }

    private fun populateCurrentWeather(myCurrentWeatherResponse: MyCurrentWeatherResponse) {

        binding.descriptionTv.text = myCurrentWeatherResponse.weather[0].description
        binding.country.text = myCurrentWeatherResponse.sys.country

        binding.tempTv.text = myCurrentWeatherResponse.main.temp?.toInt().toString() + DEGREES
        binding.cityTv.text = myCurrentWeatherResponse.name

        binding.timeTv.text =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()).toString()
        Icons.setWeatherIcon(binding.iconImageView, myCurrentWeatherResponse.weather[0].icon)

        binding.humidityTv.text =
            myCurrentWeatherResponse.main.humidity.toString() + " %" + " \nhumidity"
    }

    private val airLocation = AirLocation(this, object : AirLocation.Callback {

        override fun onSuccess(locations: ArrayList<Location>) {
            //load initial location data

            preferenceHelper.putLat(locations[0].latitude.toString())
            preferenceHelper.putLong(locations[0].longitude.toString())
            getCurrentLocationWeather(
                locations[0].latitude.toString(),
                locations[0].longitude.toString()
            ).toString()

            // observe change
            observeConversion(locations)
        }

        override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
            getCurrentLocationWeather(
                preferenceHelper.isLat,
                preferenceHelper.isLong
            ).toString()
        }
    }, true)


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        airLocation.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun observeConversion(locations: ArrayList<Location>) =
        viewModel.isConverted.observe(this, {

            if (it) {
                getCurrentLocationWeatherInPortuguese(
                    locations[0].latitude.toString(),
                    locations[0].longitude.toString()
                )
            } else {
                getCurrentLocationWeather(
                    locations[0].latitude.toString(),
                    locations[0].longitude.toString()
                ).toString()
            }
        })

    private fun setupRecyclerView() {

    }
}




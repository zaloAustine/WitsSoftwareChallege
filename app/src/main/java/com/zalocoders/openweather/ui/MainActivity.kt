package com.zalocoders.openweather.ui

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
    private val DEGREES = "\u00B0"
    lateinit var weatherWeatherAdapter: WeatherAdapter

    @Inject
    lateinit var preferenceHelper: PreferenceHelper
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        weatherWeatherAdapter = WeatherAdapter()
        binding.weatherList.recyclerview.adapter = weatherWeatherAdapter

        binding.weatherList.swipeToRefresh.setOnRefreshListener {
            getCitiesWeather()
            getCurrentLocationWeather(
                preferenceHelper.isLat,
                preferenceHelper.isLong
            )
        }

        getCitiesWeather()
        checkSwitchValue()
        airLocation.start()
        observeCurrentWeather()
        observeWeatherOfTenCities()
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

            binding.weatherList.swipeToRefresh.isRefreshing = false
            when (it) {
                is Resource.Success -> {

                    populateCurrentWeather(it.value)
                    binding.progressBar.visibility = View.GONE

                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT)
                        .show()

                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Toast.makeText(this, "An error occurred Swipe to refresh", Toast.LENGTH_SHORT)

                }
                else -> {

                }
            }
        })
    }

    private fun getCitiesWeather() {
        viewModel.getWeatherOfTenEuropeanCities("2267056,3117735,4246659,4500771,2618425,3169070,3846616,4192205,2264341,2761369")
    }

    private fun getCitiesWeatherInPortuguese() {
        viewModel.getWeatherOfTenEuropeanCitiesInPortuguese("2267056,3117735,4246659,4500771,2618425,3169070,3846616,4192205,2264341,2761369")
    }

    private fun observeWeatherOfTenCities() {
        viewModel.multipleWeatherLiveData.observe(this, {
            binding.weatherList.swipeToRefresh.isRefreshing = false

            when (it) {
                is Resource.Success -> {
                    weatherWeatherAdapter.submitList(it.value.list)
                }
                is Resource.Failure -> {
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    Toast.makeText(this, "An error occurred Swipe to refresh", Toast.LENGTH_SHORT)
                        .show()

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

            getCitiesWeather()

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

                getCitiesWeatherInPortuguese()
            } else {
                getCurrentLocationWeather(
                    locations[0].latitude.toString(),
                    locations[0].longitude.toString()
                )

                getCitiesWeather()
            }
        })

}




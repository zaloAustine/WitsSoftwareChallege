package com.zalocoders.openweather.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.zalocoders.openweather.Adapters.WeatherAdapter
import com.zalocoders.openweather.R
import com.zalocoders.openweather.commons.Resource
import com.zalocoders.openweather.databinding.ActivityMainBinding
import com.zalocoders.openweather.models.MyCurrentWeatherResponse
import com.zalocoders.openweather.ui.base.BindingActivity
import com.zalocoders.openweather.utils.ApiConstants
import com.zalocoders.openweather.utils.Icons
import com.zalocoders.openweather.utils.PreferenceHelper
import com.zalocoders.openweather.utils.Uiutils
import dagger.hilt.android.AndroidEntryPoint
import mumayank.com.airlocationlibrary.AirLocation
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>() {

    private val viewModel: WeatherViewModel by viewModels()
    lateinit var weatherWeatherAdapter: WeatherAdapter

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this

        setUpRecyclerview()

        airLocation.start()

        getCitiesWeather()
        observeWeatherOfTenCities()
        observeCurrentWeather()

        checkSwitchValue()
        moveToMyLocation()
        swipeToRefresh()

    }

    private fun setUpRecyclerview() {
        weatherWeatherAdapter = WeatherAdapter { coordinates ->
            binding.myLocation.visibility = View.VISIBLE
            getCurrentLocationWeather(
                coordinates.lat.toString(),
                coordinates.lon.toString()
            )
            expandAppBarLayout()
        }

        binding.weatherList.recyclerview.itemAnimator = null
        binding.weatherList.recyclerview.adapter = weatherWeatherAdapter
        binding.weatherList.recyclerview.addItemDecoration(object : RecyclerView.ItemDecoration() {

            //set spacing between items
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State,
            ) {
                super.getItemOffsets(outRect, view, parent, state)

                view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                view.layoutParams.height = 80.dp

                if (parent.getChildAdapterPosition(view) >= 0) {
                    outRect.top = 8.dp
                    outRect.bottom = 8.dp
                    outRect.left = 12.dp
                    outRect.right = 12.dp
                }

            }
        })
    }

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

    /*this method enable user to view weather of his location after
    /navigation through other locations*/
    private fun moveToMyLocation() {
        binding.myLocation.setOnClickListener {
            getCurrentLocationWeather(
                preferenceHelper.isLat.trim(),
                preferenceHelper.isLong.trim()
            )

            expandAppBarLayout()
            binding.myLocation.visibility = View.GONE
            Uiutils.showSnackBar(it, "Weather set to Current location")
        }
    }

    private fun checkSwitchValue() {
        binding.languageSwitch.setOnCheckedChangeListener { compoundButton, _ ->
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
                    Toast.makeText(
                        this,
                        "An Error Occurred check Internet\n Swipe to refresh",
                        Toast.LENGTH_SHORT
                    )
                        .show()
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
        viewModel.getWeatherOfTenEuropeanCities(getString(R.string.multipleCitesId))
    }

    private fun getCitiesWeatherInPortuguese() {
        viewModel.getWeatherOfTenEuropeanCitiesInPortuguese(getString(R.string.multipleCitesId))
    }

    private fun observeWeatherOfTenCities() {
        viewModel.multipleWeatherLiveData.observe(this, {
            binding.weatherList.swipeToRefresh.isRefreshing = false

            when (it) {
                is Resource.Success -> {
                    weatherWeatherAdapter.submitList(it.value.list)
                    binding.progressBar.visibility = View.GONE

                }
                is Resource.Failure -> {
                    Toast.makeText(this, "An Error Occurred check Internet", Toast.LENGTH_SHORT)
                        .show()
                    binding.progressBar.visibility = View.GONE


                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE


                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun populateCurrentWeather(myCurrentWeatherResponse: MyCurrentWeatherResponse) {

        binding.descriptionTv.text = myCurrentWeatherResponse.weather[0].description

        binding.tempTv.text =
            myCurrentWeatherResponse.main.temp?.toInt().toString() + ApiConstants.DEGREES
        binding.cityTv.text = myCurrentWeatherResponse.name

        binding.timeTv.text =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()).toString()
        Icons.setWeatherIcon(binding.iconImageView, myCurrentWeatherResponse.weather[0].icon)

    }

    // requests and gets user location
    private val airLocation = AirLocation(this, object : AirLocation.Callback {

        override fun onSuccess(locations: ArrayList<Location>) {
            //load initial location data

            preferenceHelper.putLat(locations[0].latitude.toString())
            preferenceHelper.putLong(locations[0].longitude.toString())

            getCurrentLocationWeather(
                locations[0].latitude.toString(),
                locations[0].longitude.toString()
            )

            getCitiesWeather()
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
                Uiutils.showSnackBar(binding.root, "Language set to portuguese")

            } else {
                getCurrentLocationWeather(
                    locations[0].latitude.toString(),
                    locations[0].longitude.toString()
                )

                getCitiesWeather()
                Uiutils.showSnackBar(binding.root, "Language set to English")
            }
        })

    private fun expandAppBarLayout() {
        binding.appBar.setExpanded(true, true)
    }

    override val layoutResId: Int
        get() = R.layout.activity_main

    private fun swipeToRefresh() {
        binding.weatherList.swipeToRefresh.setOnRefreshListener {
            getCitiesWeather()
            getCurrentLocationWeather(
                preferenceHelper.isLat,
                preferenceHelper.isLong
            )
        }
    }

}




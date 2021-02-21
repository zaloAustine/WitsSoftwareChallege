package com.zalocoders.openweather.utils

import android.content.Context
import com.zalocoders.openweather.utils.ApiConstants.PREFS_ATTENDANT_INFO_KEY
import com.zalocoders.openweather.utils.ApiConstants.PREFS_LAT
import com.zalocoders.openweather.utils.ApiConstants.PREFS_LONG
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceHelper @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences(PREFS_ATTENDANT_INFO_KEY, Context.MODE_PRIVATE)

    fun putLat(lat: String) {
        val edit = sharedPreferences.edit()
        edit.putString(PREFS_LAT, lat)
        edit.apply()
    }

    val isLat: String
        get() = sharedPreferences.getString(PREFS_LAT, "")!!

    fun putLong(long: String) {
        val edit = sharedPreferences.edit()
        edit.putString(PREFS_LAT, long)
        edit.apply()
    }

    val isLong: String
        get() = sharedPreferences.getString(PREFS_LONG, "")!!


}

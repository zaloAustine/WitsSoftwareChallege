package com.zalocoders.openweather.utils

import android.Manifest
import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

object Permissions {
     fun isLocationGranted(
        context: Context
    ): Boolean {
        val perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        return EasyPermissions.hasPermissions(context, *perms)
    }
}
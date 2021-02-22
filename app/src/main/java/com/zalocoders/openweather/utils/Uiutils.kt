package com.zalocoders.openweather.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

object Uiutils {

    fun showSnackBar(view: View, message: String) {
        val snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_LONG)
            .setAction("OK") {
            }

        snackbar.show()
    }
}
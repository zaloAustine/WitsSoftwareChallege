package com.zalocoders.openweather.commons


sealed class Resource<out T> {

    data class Success<out T>(val value: T) : Resource<T>()

    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: String
    ) : Resource<Nothing>()

    data class Loading(val isLoading: Boolean) : Resource<Nothing>()
}
package com.zalocoders.openweather.repositories

import com.zalocoders.openweather.commons.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {

        return withContext(Dispatchers.IO) {
            try {
                Resource.Loading(true)
                Resource.Success(apiCall.invoke())

            } catch (throwable: Throwable) {
                Resource.Loading(false)
                when (throwable) {

                    is HttpException -> {
                        Resource.Failure(
                            true,
                            throwable.code(),
                            throwable.localizedMessage.toString()
                        )
                    }
                    else -> {
                        Resource.Failure(false, null, throwable.localizedMessage.toString())
                    }
                }
            }
        }

    }
}
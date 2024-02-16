package com.example.afnews.data.network

//data class NetworkResult<out T>(
sealed class NetworkResult<T>(
//    val status: Status,
    val data: T?,
    val message: String? = null
) {

    class Success<T>(data: T) : NetworkResult<T>(data)

    class Error<T>(data: T?, msg: String) : NetworkResult<T>(data = null, msg)

    class Loading<T>(data: T) : NetworkResult<T>(data)
}
package com.cyntra.kds.data.network

import com.cyntra.kds.data.model.response.AbstractResponse
import com.cyntra.kds.data.network.util.GsonFactory
import retrofit2.HttpException
import retrofit2.Response

sealed interface NetworkResult<T>

data class Success<T>(val data: T) : NetworkResult<T>
data class Error<T>(val code: Int, val message: String?) :
    NetworkResult<T>
data class Exception<T>(val exception: Throwable) :
    NetworkResult<T>

suspend fun <T : Any> handleApi(
    execute: suspend () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Success(body)
        } else {
            val errorString = response.errorBody()?.string()
            val baseResponse = GsonFactory.getInstance()
                .fromJson(errorString, AbstractResponse::class.java)
            Error(
                code = response.code(),
                message = baseResponse.message
            )
        }
    } catch (e: HttpException) {
        Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        e.printStackTrace()
        Exception(e)
    }
}
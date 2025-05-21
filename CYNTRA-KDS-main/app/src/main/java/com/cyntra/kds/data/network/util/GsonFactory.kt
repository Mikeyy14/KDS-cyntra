package com.cyntra.kds.data.network.util

import com.google.gson.Gson

object GsonFactory {
    private var instance: Gson? = null
    fun getInstance(): Gson {
        return synchronized(this) {
            instance ?: Gson().also {
                instance = it
            }
        }
    }

    fun newInstance() = Gson()
}
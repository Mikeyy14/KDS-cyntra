package com.cyntra.kds.data.model.response

data class CrashErrorResponse(
    val `data`: Data,
    val message: String,
    val status: Boolean
)

data class Data(
    val _id: String,
    val created_at: String,
    val details: String,
    val device_id: String,
    val source: String,
    val store_id: String
)
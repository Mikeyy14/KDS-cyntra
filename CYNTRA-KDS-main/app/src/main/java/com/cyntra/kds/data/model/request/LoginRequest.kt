package com.cyntra.kds.data.model.request

data class LoginRequest(
    val emp_no: String,
    val password: String,
    val source: String,
    val device_id: String
)
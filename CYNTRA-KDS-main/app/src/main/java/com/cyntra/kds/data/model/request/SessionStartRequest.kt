package com.cyntra.kds.data.model.request

data class SessionStartRequest(
    val store_id: String,
    val emp_no: String,
    val float_amount: Int,
    val status: String
)
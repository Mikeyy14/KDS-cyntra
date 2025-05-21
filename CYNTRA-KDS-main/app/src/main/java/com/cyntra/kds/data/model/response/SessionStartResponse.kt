package com.cyntra.kds.data.model.response

data class SessionStartResponse(
    val data: SessionStartData,
    val message: String,
    val status: Boolean
)


data class SessionStartData(
    val business_date: String,
    val created_at: String,
    val emp_no: String,
    val float_amount: Int,
    val in_date_time: String,
    val out_date_time: Any,
    val session_id: String,
    val session_status: String,
    val status: String,
    val store_id: String,
    val store_status_id: String
)
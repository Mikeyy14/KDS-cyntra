package com.cyntra.kds.data.model.request

data class SessionCloseRequest(
    val amount_collected: Int,
    val change_returned: Int,
    val emp_no: String,
    val float_amount: Int,
    val float_collection: FloatCollection,
    val session_id: String,
    val status: String,
    val store_id: String,
    val store_status_id: String,
    val tenders: List<Tender>
)

data class FloatCollection(
    val coins: List<Any>,
    val cotes: List<Any>
)

data class Tender(
    val collected_amount: Int,
    val is_manual_update: Boolean,
    val is_system: Boolean,
    val system_amount: Int,
    val tnd_code: String,
    val tnd_des: String,
    val tnd_type: String
)
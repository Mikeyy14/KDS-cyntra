package com.cyntra.kds.data.model.response

data class SessionCloseResponse(
    val data: SessionCloseData,
    val message: String,
    val status: Boolean
)

class SessionCloseData
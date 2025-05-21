package com.cyntra.kds.data.repository

import com.cyntra.kds.constants.DEVICE_ID
import com.cyntra.kds.constants.SOURCE
import com.cyntra.kds.data.network.handleApi
import com.cyntra.kds.data.model.request.SessionStartRequest
import com.cyntra.kds.data.model.response.SessionStartResponse
import com.cyntra.kds.data.network.service.SessionService
import com.cyntra.kds.ui.common.util.Util
import com.cyntra.kds.ui.common.util.convertToJson
import com.cyntra.kds.data.model.request.CrashErrorRequest
import com.cyntra.kds.data.model.response.DeviceLogResponse
import com.google.gson.Gson
import javax.inject.Inject

class SessionRepository @Inject constructor(private val sessionService: SessionService) :
    BaseRepository() {

    suspend fun doSessionClose() = handleApi {
        sessionService.doSessionClose(Util(sharedPreferenceUtil).createSessionCloseRequest())
    }

    suspend fun doSessionStart(employeeId: String) = handleApi {
        sessionService.doSessionStart(
            SessionStartRequest(
                sharedPreferenceUtil.storeId,
                employeeId,
                0,
                "In"
            )
        )
    }

    suspend fun getDeviceLogReport(deviceLog: String) =
        handleApi {
            sessionService.getDeviceLogReport(
                CrashErrorRequest(
                    deviceLog,
                    DEVICE_ID,
                    SOURCE,
                    sharedPreferenceUtil.storeId
                )
            )
        }


    fun saveSessionStartDetails(it: SessionStartResponse) {
        sharedPreferenceUtil.sessionStartResponse = convertToJson(it)
        sharedPreferenceUtil.sessionId = it.data.session_id
    }

    fun saveDeviceLogResponse(it: DeviceLogResponse) {
        sharedPreferenceUtil.deviceLogReport = Gson().toJson(it)
    }

    fun clearSharedPreference() {
        sharedPreferenceUtil.clear()
    }

}
package com.cyntra.kds.data.network.service


import com.cyntra.kds.constants.network.CRASH_ERROR_URL
import com.cyntra.kds.data.model.request.CrashErrorRequest
import com.cyntra.kds.data.model.response.CrashErrorResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CrashErrorService {

    @POST(CRASH_ERROR_URL)
    suspend fun getCrashError(
        @Body crashErrorReq: CrashErrorRequest
    ): Response<CrashErrorResponse>

}
package com.cyntra.kds.data.network.service

import com.cyntra.kds.constants.network.LOGIN_URL
import com.cyntra.kds.data.model.request.LoginRequest
import com.cyntra.kds.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST(LOGIN_URL)
    suspend fun doLogin(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>
}
package com.cyntra.kds.data.repository

import com.cyntra.kds.constants.DEVICE_ID
import com.cyntra.kds.constants.SOURCE
import com.cyntra.kds.data.network.handleApi
import com.cyntra.kds.data.model.request.LoginRequest
import com.cyntra.kds.data.model.response.LoginResponse
import com.cyntra.kds.data.network.service.LoginService
import com.cyntra.kds.ui.common.util.convertToJson
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginService: LoginService,
) : BaseRepository() {

    suspend fun doLogin(employeeNumber: String, password: String) =
        handleApi {
            loginService.doLogin(LoginRequest(employeeNumber, password, SOURCE, DEVICE_ID))
        }

    fun saveLoginDetails(it: LoginResponse) {
        sharedPreferenceUtil.storeId = it.data.store_details.store_id
        sharedPreferenceUtil.sessionId = it.data.session_id
        sharedPreferenceUtil.storeDetails = convertToJson(it.data.store_details)
        sharedPreferenceUtil.employeeNumber = convertToJson(it.data.employee.emp_no)
        sharedPreferenceUtil.loginResponse = convertToJson(it)
    }
}
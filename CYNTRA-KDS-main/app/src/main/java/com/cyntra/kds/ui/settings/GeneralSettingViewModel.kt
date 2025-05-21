package com.cyntra.kds.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cyntra.kds.data.model.response.SessionCloseResponse
import com.cyntra.kds.data.network.Success
import com.cyntra.kds.data.repository.SessionRepository
import com.cyntra.kds.ui.base.BaseViewModel
import com.cyntra.kds.data.model.response.DeviceLogResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralSettingViewModel @Inject constructor(private val repository: SessionRepository) :
    BaseViewModel(repository) {

    private var _sessionCloseResponse: MutableLiveData<SessionCloseResponse> = MutableLiveData()
    val sessionCloseResponse get() = _sessionCloseResponse

    fun doSessionClose() {
        mProgress.postValue(true)
        viewModelScope.launch {
            val response = repository.doSessionClose()
            mProgress.postValue(false)
            when (response) {
                is Success -> response.data.let {
                    repository.clearSharedPreference()
                    _sessionCloseResponse.postValue(it)
                }

                else -> handleError(response)
            }
        }
    }


    private var _deviceLogResponse: MutableLiveData<DeviceLogResponse> = MutableLiveData()
    val deviceLogResponse get() = _deviceLogResponse
    fun getDeviceLogReport(deviceLog: String) {
        mProgress.postValue(true)
        viewModelScope.launch {
            val response = repository.getDeviceLogReport(deviceLog)
            mProgress.postValue(false)
            when (response) {
                is Success -> {
                    response.data.let {
                        repository.saveDeviceLogResponse(it)
                        _deviceLogResponse.postValue(it)
                        successfulMessage.postValue("Log Successfully Uploaded")
                    }
                }

                else -> handleError(response)
            }
        }
    }
}
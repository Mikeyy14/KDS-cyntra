package com.cyntra.kds.ui.crashhandler

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cyntra.kds.data.model.response.CrashErrorResponse
import com.cyntra.kds.data.network.Success
import com.cyntra.kds.data.repository.BaseRepository
import com.cyntra.kds.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrashErrorViewModel @Inject constructor(private val repository: BaseRepository) :
    BaseViewModel(repository) {

    private var _crashErrorResponse: MutableLiveData<CrashErrorResponse> = MutableLiveData()
    val crashErrorResponse get() = _crashErrorResponse
    fun getCrashError(error: String) {
        mProgress.postValue(true)
        viewModelScope.launch {
            val response = repository.getCrashError(error)
            mProgress.postValue(false)
            when (response) {
                is Success -> {
                    response.data.let {
                        repository.saveCrashErrorResponse(it)
                        _crashErrorResponse.postValue(it)
                    }
                }

                else -> handleError(response)
            }
        }
    }

}
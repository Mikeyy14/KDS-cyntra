package com.cyntra.kds.ui.settings.printersetting

import com.cyntra.kds.data.repository.BaseRepository
import com.cyntra.kds.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrinterSettingViewModel @Inject constructor( repository: BaseRepository): BaseViewModel(repository) {

    companion object {
        const val TAG = "PrinterSettingViewModel"
    }
}
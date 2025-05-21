package com.cyntra.kds.ui.base

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cyntra.kds.ui.common.dialog.ProgressDialogUtils
import com.cyntra.kds.ui.common.listener.CommonListener
import com.cyntra.kds.ui.common.util.Util.Companion.helpDialog
import com.cyntra.kds.ui.common.util.isNoInternetConnection
import com.cyntra.kds.ui.common.util.showToast
import com.cyntra.kds.ui.crashhandler.ExceptionHandler
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
open class BaseActivity : AppCompatActivity(),
    CommonListener {

    lateinit var baseViewModel: BaseViewModel
    lateinit var helpDialog: Dialog
    val TAG by lazy { baseViewModel.activityName }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        helpDialog = helpDialog()
        isNoInternetConnection()
    }


    open fun observers() {
        baseViewModel.mProgress.observe(this)
        {
            if (it) {
                ProgressDialogUtils.getInstance().showProgress(this, false)
            } else {
                ProgressDialogUtils.getInstance().hideProgress()
            }
        }
        baseViewModel.errorMessageId.observe(this) {
            showToast(getString(it))
        }
        baseViewModel.errorMessage.observe(this) {
            showToast(it)
        }
    }

}
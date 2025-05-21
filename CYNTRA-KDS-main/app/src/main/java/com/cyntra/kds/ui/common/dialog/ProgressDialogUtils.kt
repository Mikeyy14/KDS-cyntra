package com.cyntra.kds.ui.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import com.cyntra.kds.R

class ProgressDialogUtils {

    private var mDialog: Dialog? = null
    companion object {
        private var progressDialog: ProgressDialogUtils? = null
        fun getInstance(): ProgressDialogUtils {
            if (progressDialog == null) {
                progressDialog = ProgressDialogUtils()
            }
            return progressDialog as ProgressDialogUtils
        }
    }

    fun showProgress(context: Context, cancelable: Boolean) {
        try {
            if (mDialog == null) {
                mDialog = Dialog(context)
                mDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mDialog?.setContentView(R.layout.item_progress_dialog)
                mDialog?.setCancelable(cancelable)
                mDialog?.setCanceledOnTouchOutside(cancelable)
                mDialog?.show()
            } else {
                mDialog?.setCancelable(cancelable)
                mDialog!!.show()
            }
        } catch (e: Exception) {
            e.message?.let { Log.e("MyExceptions", it) }
        }
    }

    fun hideProgress() {
        try {
            if (mDialog != null) {
                mDialog?.dismiss()
                mDialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

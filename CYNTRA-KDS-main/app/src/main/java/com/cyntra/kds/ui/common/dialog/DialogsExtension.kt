package com.cyntra.kds.ui.common.dialog

import android.app.Activity
import android.app.Dialog
import android.view.Window
import com.cyntra.kds.R
import com.cyntra.kds.databinding.DialogSessionEndLayoutBinding

fun Activity.showSessionEndDialog(onYesClicked : ()-> Unit): Dialog {
    val sessionEndDialog = Dialog(this)
    sessionEndDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    sessionEndDialog.setCancelable(false)
    val dialogBinding = DialogSessionEndLayoutBinding.inflate(layoutInflater)
    sessionEndDialog.setContentView(dialogBinding.root)

    dialogBinding.btnNo.setOnClickListener {
        sessionEndDialog.dismiss()
    }
    dialogBinding.btnYes.setOnClickListener {
        sessionEndDialog.dismiss()
        onYesClicked()
    }
    sessionEndDialog.window?.setBackgroundDrawableResource(R.drawable.background_dialog_grey)
    return sessionEndDialog
}


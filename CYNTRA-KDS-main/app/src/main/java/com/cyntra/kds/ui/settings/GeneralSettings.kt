package com.cyntra.kds.ui.settings

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.cyntra.kds.BuildConfig
import com.cyntra.kds.databinding.ActivityGeneralSettingsBinding
import com.cyntra.kds.ui.base.BaseActivity
import com.cyntra.kds.ui.common.dialog.showSessionEndDialog
import com.cyntra.kds.ui.common.util.Util.Companion.readLogFileContent
import com.cyntra.kds.ui.common.util.Util.Companion.writeLogResultKeyPairUseAppendMode
import com.cyntra.kds.ui.common.util.moveToScreen
import com.cyntra.kds.ui.common.util.moveToScreenWithFinish
import com.cyntra.kds.ui.common.util.showToast
import com.cyntra.kds.ui.login.LoginActivity
import com.cyntra.kds.ui.settings.printersetting.PrinterSetting
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeneralSettings : BaseActivity() {
    private val binding by lazy { ActivityGeneralSettingsBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<GeneralSettingViewModel>()
    private lateinit var sessionCloseDialog: Dialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        setUpClickListeners()

        writeLogResultKeyPairUseAppendMode(
            "General setting screen....",
            true
        )
    }

    @SuppressLint("SetTextI18n")
    fun init() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        baseViewModel = viewModel
        viewModel.activityName = TAG
        binding.appVersionName.text = "v-${BuildConfig.VERSION_NAME}"
        sessionCloseDialog = showSessionEndDialog {
            viewModel.doSessionClose()
        }
        sessionCloseObserver()
        deviceLogReportObserver()
    }

      fun setUpClickListeners() {

        binding.switchPrintEnabled.isChecked = viewModel.sharedPreferenceUtil.printReceiptEnabled
        binding.switchPrintEnabled.setOnCheckedChangeListener { _, _ ->
            if (viewModel.sharedPreferenceUtil.printReceiptEnabled) {
                viewModel.sharedPreferenceUtil.printReceiptEnabled = false
                showToast("Print has been disabled!")
                writeLogResultKeyPairUseAppendMode(
                    "Print has been disabled!",
                    true
                )
            } else {
                viewModel.sharedPreferenceUtil.printReceiptEnabled = true
                showToast("Print has been enabled!")
                writeLogResultKeyPairUseAppendMode(
                    "Print has been enabled!",
                    true
                )
            }
        }
        binding.rlPrinterSettings.setOnClickListener {
            writeLogResultKeyPairUseAppendMode(
                "Clicked printer setting button",
                true
            )
            moveToScreenWithFinish(
                PrinterSetting::class.java,
                "GeneralSettings"
            )
        }

        binding.ivBackBtn.setOnClickListener {
            writeLogResultKeyPairUseAppendMode(
                "Clicked back button",
                true
            )
            moveToScreen(LoginActivity::class.java, "")
        }
        binding.btnEndSession.setOnClickListener {
            writeLogResultKeyPairUseAppendMode(
                "Clicked end session button",
                true
            )
            sessionCloseDialog.show()
        }

        binding.rlUploadLog.setOnClickListener{
            writeLogResultKeyPairUseAppendMode(
                "Clicked upload log button",
                true
            )
            viewModel.getDeviceLogReport(Gson().toJson(readLogFileContent()))
            showToast("Device log successfully uploaded")
        }
    }

    private fun sessionCloseObserver() {
        viewModel.sessionCloseResponse.observe(this) {
            it.let {
                startActivity(Intent(this, LoginActivity::class.java)).apply { finish()
                }
            }
        }
    }



    private fun deviceLogReportObserver(){
        viewModel.deviceLogResponse.observe(this){}

    }



    companion object {
        const val TAG = "GeneralSettings"
    }
}
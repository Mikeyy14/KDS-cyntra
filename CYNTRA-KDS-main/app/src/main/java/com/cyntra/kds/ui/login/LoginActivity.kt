package com.cyntra.kds.ui.login

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.cyntra.kds.R
import com.cyntra.kds.constants.LOGIN_ACTIVITY
import com.cyntra.kds.databinding.ActivityLoginBinding
import com.cyntra.kds.ui.base.BaseActivity
import com.cyntra.kds.ui.common.util.Util.Companion.writeLogResultKeyPairUseAppendMode
import com.cyntra.kds.ui.common.util.isAllGranted
import com.cyntra.kds.ui.common.util.multiplePermissionDenied
import com.cyntra.kds.ui.common.util.multiplePermissions
import com.cyntra.kds.ui.common.util.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LoginViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        observers()
        setUpClickListeners()
        checkPermission()
        writeLogResultKeyPairUseAppendMode(
            "Login Screen... ",
            true
        )
    }

    fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        baseViewModel = viewModel
        viewModel.activityName = LoginActivity.TAG
    }

    private val permissions by multiplePermissions {
        when {
            it.isAllGranted() -> {
                viewModel.setIsPermissionCheck(true)
            }

            else -> {
                viewModel.setIsPermissionCheck(false)
                multiplePermissionDenied()
            }
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.launch(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.CAMERA
                )
            )
        } else {
            permissions.launch(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
            )
        }
    }


    override fun observers() {
        super.observers()
        loginObserver()
        sessionStartObserver()
    }

    private fun loginObserver() {
        writeLogResultKeyPairUseAppendMode(
            "Login Screen :: Login API Calling....",
            true
        )
        viewModel.loginResponse.observe(this) {
            it.let {
                if (it.data.session_id.isEmpty()) {
                   viewModel.doSessionStart()
                }
            }
        }
    }

    private fun sessionStartObserver() {
        writeLogResultKeyPairUseAppendMode(
            "Login Screen :: Session start API Calling....",
            true
        )
        viewModel.sessionStartResponse.observe(this) {

        }
    }

    fun setUpClickListeners() {
        binding.signInBtn.setOnClickListener {
            if (!viewModel.isPermissionCheck.value) {
                checkPermission()
            } else {
                if (isValidate()) {
                    writeLogResultKeyPairUseAppendMode(
                        "Cashier Id: ${viewModel.employeeId.value} \n Password: ${viewModel.password.value}",
                        true
                    )
                    writeLogResultKeyPairUseAppendMode(
                        "Cashier Sign In clicked and moved to next screen",
                        true
                    )
                    viewModel.doLogin()
                }
            }
        }
        binding.cashierIdCrossImg.setOnClickListener {
            if (viewModel.employeeId.value.isNotEmpty()) {
                viewModel.employeeId.value = ""
            }
        }
        binding.passwordCrossImg.setOnClickListener {
            if (viewModel.password.value.isNotEmpty()) {
                viewModel.password.value = ""
            }
        }
    }

    fun isValidate(): Boolean {
        if (viewModel.employeeId.value.isEmpty()) {
            showToast(getString(R.string.please_enter_cashier_id))
            writeLogResultKeyPairUseAppendMode(
                getString(R.string.please_enter_cashier_id),
                true
            )
            return false
        } else if (viewModel.password.value.isEmpty()) {
            showToast(getString(R.string.please_enter_password))
            writeLogResultKeyPairUseAppendMode(
                getString(R.string.please_enter_password),
                true
            )
            return false
        }
        return true
    }

    companion object {
        const val TAG = LOGIN_ACTIVITY
    }

}


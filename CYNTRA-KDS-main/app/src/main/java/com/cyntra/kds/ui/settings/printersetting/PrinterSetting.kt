package com.cyntra.kds.ui.settings.printersetting

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import com.cyntra.kds.databinding.ActivityPrinterSettingBinding
import com.cyntra.kds.printer.async.AsyncEscPosPrint
import com.cyntra.kds.printer.async.AsyncEscPosPrinter
import com.cyntra.kds.printer.async.AsyncTcpEscPosPrint
import com.cyntra.kds.printer.epson.DeviceList
import com.cyntra.kds.printer.epson.DeviceList.Model
import com.cyntra.kds.printer.epson.DeviceList.Port
import com.cyntra.kds.ui.base.BaseActivity
import com.cyntra.kds.ui.common.util.Util.Companion.writeLogResultKeyPairUseAppendMode
import com.cyntra.kds.ui.common.util.moveToScreenWithFinish
import com.cyntra.kds.ui.settings.GeneralSettings
import com.dantsu.escposprinter.connection.DeviceConnection
import com.dantsu.escposprinter.connection.tcp.TcpConnection
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrinterSetting : BaseActivity() {
    private val binding by lazy { ActivityPrinterSettingBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<PrinterSettingViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        setUpClickListeners()

        writeLogResultKeyPairUseAppendMode(
            "Printer setting screen....",
            true
        )
    }

    fun init() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        baseViewModel = viewModel
        binding.etKioskPrinterInput.setText(viewModel.sharedPreferenceUtil.kioskPrinterIP)
    }

    fun setUpClickListeners() {

        binding.etKioskPrinterInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(input: Editable?) {
                if (input!!.isNotEmpty()) {
                    binding.ivCrossKioskIp.visibility = View.VISIBLE
                } else {
                    binding.ivCrossKioskIp.visibility = View.INVISIBLE
                }
            }

        })
        binding.etKitchenPrinterInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    binding.ivCrossKitchenIp.visibility = View.VISIBLE
                } else {
                    binding.ivCrossKitchenIp.visibility = View.INVISIBLE
                }
            }
        })
        binding.ivBtnBackPrinterSetting.setOnClickListener {
            writeLogResultKeyPairUseAppendMode(
                "Clicked back button",
                true
            )
            moveToScreenWithFinish(GeneralSettings::class.java, "")
        }
        binding.ivCrossKitchenIp.setOnClickListener {
            binding.etKitchenPrinterInput.text.clear()
        }
        binding.ivCrossKioskIp.setOnClickListener {
            binding.etKioskPrinterInput.text.clear()
        }
        binding.btnKioskPrinterTest.setOnClickListener {
            writeLogResultKeyPairUseAppendMode(
                "Clicked kitchen printer test button",
                true
            )
            val ip = binding.etKioskPrinterInput.text.toString().trim()
            printTcp(ip)
            writeLogResultKeyPairUseAppendMode(
                "Kitchen printer ip: $ip",
                true
            )
        }
        binding.btnKioskPrinterApply.setOnClickListener {
            writeLogResultKeyPairUseAppendMode(
                "Kitchen printer apply button",
                true
            )
            var port = Port.All
            when (binding.spinnerKioskPrinterType.selectedItem.toString().trim()) {
                Port.All.toString() -> port = Port.All
                Port.TCP.toString() -> port = Port.TCP
                Port.USB.toString() -> port = Port.USB
            }
            var model = Model.All
            when (binding.spinnerKioskPrinterConnectionType.selectedItem.toString().trim()) {
                Model.All.toString() -> model = Model.All
                Model.Epson.toString() -> model = Model.Epson
                Model.PosIn.toString() -> model = Model.PosIn
            }
            binding.etKioskPrinterInput.text.clear()
            if (model == Model.Epson) {
                searchEpsonPrinter(binding.etKioskPrinterInput, model, port)
                Log.d("###kiosk printer", binding.etKitchenPrinterInput.toString())
            }
        }
    }

    private fun searchEpsonPrinter(
        et_printer_input: EditText,
        model: Model,
        port: Port
    ) {
        DeviceList.dialog(this, et_printer_input, model, port)
        { name, _, _, _ ->
            Log.d("searchEpsonPrinter ", name)
        }
    }

    private fun printTcp(ip: String = "192.168.1.34") {
        val portAddress = 9100
        try {
            AsyncTcpEscPosPrint(
                this,
                object : AsyncEscPosPrint.OnPrintFinished() {
                    override fun onError(
                        asyncEscPosPrinter: AsyncEscPosPrinter?,
                        codeException: Int
                    ) {
                        Log.e(
                            "Async.OnPrintFinished",
                            "AsyncEscPosPrint.OnPrintFinished : An error occurred !"
                        )
                    }

                    override fun onSuccess(asyncEscPosPrinter: AsyncEscPosPrinter?) {
                        viewModel.sharedPreferenceUtil.kioskPrinterIP = ip
                        Log.i(
                            "Async.OnPrintFinished",
                            "AsyncEscPosPrint.OnPrintFinished : Print is finished !"
                        )
                    }
                }
            ).execute(
                this.getAsyncEscPosPrinter(
                    TcpConnection(
                        ip, portAddress
                    )
                )
            )
        } catch (e: NumberFormatException) {
            AlertDialog.Builder(this)
                .setTitle("Invalid TCP port address")
                .setMessage("Port field must be an integer.")
                .show()
            e.printStackTrace()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getAsyncEscPosPrinter(printerConnection: DeviceConnection?): AsyncEscPosPrinter? {
        val printer = AsyncEscPosPrinter(printerConnection, 203, 48f, 48)
        return printer.addTextToPrint(getOrderStringForPrint().trimIndent())
    }

    private fun getOrderStringForPrint(): String {

        return "[L]\n" +
                "[C]================================\n" +
                "[C]<u><font size='big'>PRINTER TEST SUCCESS</font></u>\n" +
                "[L]\n" +
                "[C]================================\n"
    }

}
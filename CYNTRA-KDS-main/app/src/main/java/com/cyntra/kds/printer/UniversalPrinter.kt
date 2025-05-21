/*
    This is the last code that I'm writing for Cyntra. Maybe the code I'm doing is not of that standards, but one day it'll be.
    It was nice and great to be a part of this organization. Hope we all be doing better every next day.
    Keshri Nandan Signing off.
    30th June, 2023
 */

package com.cyntra.kds.printer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Parcelable
import android.util.Log
import com.cyntra.kds.data.local.sharedPreference.SharedPreferenceUtil
import com.cyntra.kds.printer.async.AsyncEscPosPrint
import com.cyntra.kds.printer.async.AsyncEscPosPrinter
import com.cyntra.kds.printer.async.AsyncTcpEscPosPrint
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.DeviceConnection
import com.dantsu.escposprinter.connection.tcp.TcpConnection
import com.dantsu.escposprinter.connection.usb.UsbConnection
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import javax.inject.Inject


class UniversalPrinter @Inject constructor(
    val context: Context,
    val sharedPreferenceUtil: SharedPreferenceUtil
) {

    companion object {
        const val TAG = "UniversalPrinter.kt"
        const val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
        var invoiceText = ""
        var kotTextForEpson = ""
        var userImage = ""
    }


    lateinit var printer: EscPosPrinter


    private val usbReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (ACTION_USB_PERMISSION == action) {
                synchronized(this) {
                    val usbManager = context?.getSystemService(Context.USB_SERVICE) as UsbManager
                    val usbDevice =
                        intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice?
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbDevice != null) {
                            // YOUR PRINT CODE HERE
                            printer =
                                EscPosPrinter(UsbConnection(usbManager, usbDevice), 203, 48f, 32)
                            if (!PrinterWorkAround.oncePrinted) {
                                val stringDemo = "getOrderInvoiceStringForPrint()"
                                printer.printFormattedText(stringDemo.trimIndent())
                                PrinterWorkAround.oncePrinted = true
                            }
                            if (!PrinterWorkAround.oncePrintImage) {
                                val stringImage = printImage()
                                printer.printFormattedText(stringImage.trimIndent())
                                PrinterWorkAround.oncePrintImage = true
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addTwoLine(isKot: Boolean = false) {
        if (isKot)
            kotTextForEpson += "[C]================================================\n"
        else
            invoiceText += "[C]================================\n"

    }

    private fun addSingleLine(isKot: Boolean = false) {
        if (!isKot)
            invoiceText += "[C]--------------------------------\n"
        else
            kotTextForEpson += "[C]------------------------------------------------\n"
    }

    private fun lineBreak(isKot: Boolean = false, isImage: Boolean = false) {
        invoiceText += "[L]\n"
        kotTextForEpson += "[L]\n"
        userImage += "[L]\n"
    }

    private fun addTitle(isKot: Boolean = false, title: String, alignment: Char = 'C') {
        if (!isKot)
            invoiceText += "[$alignment]<font size='big'>$title</font>\n"
        else
            kotTextForEpson += "[$alignment]<b><font size='big'>$title</font></b>\n"
    }


    private fun onAddLine(
        isKot: Boolean = false,
        text: String,
        alignment: Char = 'C',
        isBold: Boolean = false
    ) {

        if (isBold) {
            if (!isKot)
                invoiceText += "[$alignment]<b>$text</b>\n"
            else
                kotTextForEpson += "[$alignment]<b><font size='big'>$text</font></b>\n"
        } else {
            if (!isKot)
                invoiceText += "[$alignment]$text\n"
            else
                kotTextForEpson += "[$alignment]<font size='wide'>$text</font>\n"
        }
    }


    fun bitmap(): Bitmap {
        val stringArray = sharedPreferenceUtil.image
        val split = stringArray.substring(1, stringArray.length - 1).split(", ".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val array = ByteArray(split.size)
        for (i in split.indices) {
            array[i] = split[i].toByte()
        }

        return BitmapFactory.decodeByteArray(array, 0, array.size)
    }

    private fun onAddBitmap() {
        userImage = "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(
            printer,
            bitmap()
        ) + "</img>\n"
    }

    private fun onAddTwoLine(
        textLeftt: String,
        textRight: String,
        textLeftAlignment: Char = 'L',
        textRightAlignment: Char = 'R',
        printCurrency: Boolean = false,
        currency: String = "$",
        isBold: Boolean = false,
        isKot: Boolean = false
    ) {
        var textLeft = textLeftt

        if (!isKot) {
            if (textLeft.length > 26)
                textLeft = textLeft.substring(0, 26)
            invoiceText += if (isBold) {
                if (printCurrency)
                    "[$textLeftAlignment]<b>$textLeft</b>[$textRightAlignment]<b>$currency$textRight</b>\n"
                else
                    "[$textLeftAlignment]<b>$textLeft</b>[$textRightAlignment]<b>$textRight</b>\n"
            } else {
                if (printCurrency)
                    "[$textLeftAlignment]$textLeft[$textRightAlignment]$currency$textRight\n"
                else
                    "[$textLeftAlignment]$textLeft[$textRightAlignment]$textRight\n"
            }
        } else {
            if (textLeft.length > 40)
                textLeft = textLeft.substring(0, 40)
            kotTextForEpson += if (isBold) {
                if (printCurrency)
                    "[$textLeftAlignment]<b>$textLeft</b>[$textLeftAlignment]<b>$currency$textRight</b>\n"
                else
                    "[$textLeftAlignment]<b><font size='big'>$textLeft</font></b>[$textLeftAlignment]<b><font size='big'>$textRight</font></b>\n"
            } else {
                if (printCurrency)
                    "[$textLeftAlignment]$textLeft[$textLeftAlignment]$currency$textRight\n"
                else
                    "[$textLeftAlignment]<font size='wide'>$textLeft</font>[$textLeftAlignment]<font size ='wide'>$textRight</font>\n"
            }
        }
    }

    private fun printImage(): String {
        lineBreak(isKot = false, isImage = true)
        onAddBitmap()
        lineBreak(isKot = false, isImage = true)
        return userImage
    }

    fun printUsb() {
        val usbConnection: UsbConnection? = UsbPrintersConnections.selectFirstConnected(context)
        //val usbManager = this.getSystemService<UsbManager>()as UsbManager
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        if (usbConnection != null && usbManager != null) {
            val permissionIntent = PendingIntent.getBroadcast(
                context,
                0,
                Intent(ACTION_USB_PERMISSION),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
            )
            val filter = IntentFilter(ACTION_USB_PERMISSION)
            context.registerReceiver(usbReceiver, filter)
            usbManager.requestPermission(usbConnection.device, permissionIntent)
        }
    }

    fun printUsbForImage() {
        val usbConnection: UsbConnection? = UsbPrintersConnections.selectFirstConnected(context)
        //val usbManager = this.getSystemService<UsbManager>()as UsbManager
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        if (usbConnection != null && usbManager != null) {
            val permissionIntent = PendingIntent.getBroadcast(
                context,
                0,
                Intent(ACTION_USB_PERMISSION),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
            )
            val filter = IntentFilter(ACTION_USB_PERMISSION)
            context.registerReceiver(usbReceiver, filter)
            usbManager.requestPermission(usbConnection.device, permissionIntent)
        }
    }


    /*==============================================================================================
    =========================================TCP PART===============================================
    ==============================================================================================*/

    private fun printByTcp() {
        Thread {
            try {
                val printer = EscPosPrinter(TcpConnection("192.168.1.34", 9100, 1000), 203, 48f, 32)
                printer.printFormattedText(invoiceText.trimIndent())

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun printKotByTcp(ip: String = "192.168.1.37") {
        if (!PrinterWorkAround.oncePrintedEpson) {
            val ipAddress = ip
            val portAddress = 9100

            try {
                AsyncTcpEscPosPrint(
                    context,
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
                            Log.i(
                                "Async.OnPrintFinished",
                                "AsyncEscPosPrint.OnPrintFinished : Print is finished !"
                            )
                        }
                    }
                ).execute(
                    this.getAsyncEscPosPrinter(
                        TcpConnection(
                            ipAddress, portAddress
                        )
                    )
                )
            } catch (e: NumberFormatException) {
                AlertDialog.Builder(context)
                    .setTitle("Invalid TCP port address")
                    .setMessage("Port field must be an integer.")
                    .show()
                e.printStackTrace()
            }
            PrinterWorkAround.oncePrintedEpson = true
        }

    }

    /*==============================================================================================
    ===================================ESC/POS PRINTER PART=========================================
    ==============================================================================================*/

    @SuppressLint("SimpleDateFormat")
    fun getAsyncEscPosPrinter(printerConnection: DeviceConnection?): AsyncEscPosPrinter? {
        val printer = AsyncEscPosPrinter(printerConnection, 203, 48f, 48)
//        getOrderKotStringForPrint()
        return printer.addTextToPrint(kotTextForEpson.trimIndent())
    }


}
package com.cyntra.kds.ui.common.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import com.cyntra.kds.R
import com.cyntra.kds.data.local.sharedPreference.SharedPreferenceUtil
import com.cyntra.kds.data.model.response.StoreDetails
import com.cyntra.kds.data.model.request.FloatCollection
import com.cyntra.kds.data.model.request.SessionCloseRequest
import com.cyntra.kds.data.model.request.Tender
import com.cyntra.kds.data.model.response.LoginResponse
import com.google.gson.Gson
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@SuppressLint("SimpleDateFormat")
class Util @Inject constructor(val sharedPreferenceUtil: SharedPreferenceUtil) {

    private val storeDetailsResponse = Gson().fromJson(
        sharedPreferenceUtil.storeDetails, StoreDetails::class.java
    )
    private val loginResponse =
        Gson().fromJson(sharedPreferenceUtil.loginResponse, LoginResponse::class.java)


    fun createSessionCloseRequest(): SessionCloseRequest {
        val floatCollection = FloatCollection(emptyList(), emptyList())
        val tenderList = ArrayList<Tender>()
//        for (tenderItem in tenderListResponse) {
//            val tenderItemResponse = Tender(
//                collected_amount = 0,
//                is_manual_update = false,
//                is_system = true,
//                system_amount = 0,
//                tnd_code = tenderItem.tnd_code,
//                tnd_des = tenderItem.tnd_des,
//                tnd_type = tenderItem.tnd_type
//            )
//            tenderList.add(tenderItemResponse)
//        }

        return SessionCloseRequest(
            0,
            0,
            loginResponse.data.employee.emp_no,
            0,
            floatCollection,
            sharedPreferenceUtil.sessionId,
            "Out",
            sharedPreferenceUtil.storeId,
            loginResponse.data.store_session_id, tenderList
        )
    }

    companion object {

        private fun getCurrentDate(): String {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val cal = Calendar.getInstance()
            return dateFormat.format(cal.time)
        }
        fun getCurrentDateTime(): String {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd h:mm:ss")
            val cal = Calendar.getInstance()
            return dateFormat.format(cal.time)
        }

        fun getOrderDateTime(): String {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val cal = Calendar.getInstance()
            return dateFormat.format(cal.time)
        }

        fun Context.helpDialog(): Dialog {
            val dialogHelp = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            dialogHelp.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogHelp.setCancelable(false)
            dialogHelp.setContentView(R.layout.dialog_help)

            dialogHelp.findViewById<ImageView>(R.id.close_dialog_help).setOnClickListener {
                dialogHelp.dismiss()
            }

            dialogHelp.findViewById<Button>(R.id.btn_call_assistant).setOnClickListener {
                dialogHelp.dismiss()
            }
            writeLogResultKeyPairUseAppendMode(
                "Open help popup",
                true
            )
            return dialogHelp
        }




        /*** Created timestamp based log files in log folder **/
        fun Context.writeLogResultKeyPairUseAppendMode(
            line: String?,
            isAppend: Boolean
        ) {
            try {
                var fileName = getCurrentDate()
                fileName = "breez_$fileName.txt"
                //println("inside writing history -> writeLogResultForLaterUse")
                val appLogFolder = File(filesDir,"log")
                if (!appLogFolder.exists()) {
                    appLogFolder.mkdirs()
                }
                var writer: FileWriter? = null
                try {
                    val gpxfile = File(appLogFolder, fileName)
                    if (!gpxfile.exists()) {
                        gpxfile.createNewFile()
                    }
                    writer = FileWriter(gpxfile, true)
                    writer.append("\n")
                    writer.append((Date().toLocaleString()) + "    " + line)
                } catch (e: java.lang.Exception) {
                    Log.e("", "" + e.message)
                } finally {
                    if (writer != null) {
                        try {
                            writer.flush()
                            writer.close()
                        } catch (e: IOException) {
                            Log.e("", "" + e.message)
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
                Log.e("", "" + e.message)
            }
        }

        fun Context.readLogFileContent(): String {
            var fileName = getCurrentDate()
            fileName = "breez_$fileName.txt"
            val appLogFolder = File(filesDir, "log")
            val gpxfile = File(appLogFolder, fileName)
            if (!gpxfile.exists()) {
                // Handle the case where the file does not exist or any other error.
                return ""
            }
            val stringBuilder = StringBuilder()
            var bufferedReader: BufferedReader? = null
            try {
                bufferedReader = BufferedReader(FileReader(gpxfile))

                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("")
                }
            } catch (e: Exception) {
                Log.e("", "" + e.message)
            } finally {
                try {
                    bufferedReader?.close()
                } catch (e: Exception) {
                    Log.e("", "" + e.message)
                }
            }

            return stringBuilder.toString()
        }


    }
}
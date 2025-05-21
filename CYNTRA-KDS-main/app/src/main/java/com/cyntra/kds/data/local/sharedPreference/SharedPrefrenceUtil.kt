package com.cyntra.kds.data.local.sharedPreference

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPreferenceUtil(application: Application) {

    private val Tag = SharedPreferenceUtil::class.java.simpleName
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    var image: String
        get() = sharedPreferences["image", ""]!!
        set(value) = sharedPreferences.set("image", value)

    var printReceiptEnabled: Boolean
        get() = sharedPreferences["printReceiptEnabled", true]!!
        set(value) = sharedPreferences.set("printReceiptEnabled", value)

    var authToken: String
        get() = sharedPreferences["authToken", ""]!!
        set(value) = sharedPreferences.set("authToken", value)

    var storeId: String
        get() = sharedPreferences["storeId", ""]!!
        set(value) = sharedPreferences.set("storeId", value)

    var sessionId: String
        get() = sharedPreferences["sessionId", ""]!!
        set(value) = sharedPreferences.set("sessionId", value)


    var loginResponse : String
        get() = sharedPreferences["loginResponse",""]!!
        set(value) = sharedPreferences.set("loginResponse", value)

    var categoryData: String
        get() = sharedPreferences["categoryData",""]!!
        set(value) = sharedPreferences.set("categoryData", value)

    var itemData: String
        get() = sharedPreferences["itemData",""]!!
        set(value) = sharedPreferences.set("itemData", value)

    var storeDetails: String
        get() = sharedPreferences["storeDetails", ""]!!
        set(value) = sharedPreferences.set("storeDetails", value)

    var employeeNumber: String
        get() = sharedPreferences["employeeNumber", ""]!!
        set(value) = sharedPreferences.set("employeeNumber", value)

    var orderEndDateTime: String
        get() = sharedPreferences["orderEndDateTime", ""]!!
        set(value) = sharedPreferences.set("orderEndDateTime", value)

    var orderDetailRes: String
        get() = sharedPreferences["orderDetailRes", ""]!!
        set(value) = sharedPreferences.set("orderDetailRes", value)

    var orderStartDateTime: String
        get() = sharedPreferences["orderStartDateTime", ""]!!
        set(value) = sharedPreferences.set("orderStartDateTime", value)

    var orderTypeList: String
        get() = sharedPreferences["orderTypeList", ""]!!
        set(value) = sharedPreferences.set("orderTypeList", value)

    var crashError: String
        get() = sharedPreferences["crashError", ""]!!
        set(value) = sharedPreferences.set("crashError", value)

    var deviceLogReport: String
        get() = sharedPreferences["deviceLogReport", ""]!!
        set(value) = sharedPreferences.set("deviceLogReport", value)


    var tenderList: String
        get() = sharedPreferences["tenderList", ""]!!
        set(value) = sharedPreferences.set("tenderList", value)

    var sessionStartResponse: String
        get() = sharedPreferences["sessionStartResponse", ""]!!
        set(value) = sharedPreferences.set("sessionStartResponse", value)

    var id: String
        get() = sharedPreferences["_id", ""]!!
        set(value) = sharedPreferences.set("_id", value)

    var name: String
        get() = sharedPreferences["name", "Guest"]!!
        set(value) = sharedPreferences.set("name", value)

    var mobile: String
        get() = sharedPreferences["mobile", ""]!!
        set(value) = sharedPreferences.set("mobile", value)
    var email: String
        get() = sharedPreferences["email", ""]!!
        set(value) = sharedPreferences.set("email", value)

    var kioskPrinterIP: String
        get() = sharedPreferences["KioskPrinterIP", ""]!!
        set(value) = sharedPreferences.set("KioskPrinterIP", value)


    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> Log.e(Tag, "Setting shared pref failed for key: $key and value: $value ")
        }
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    fun clear() {
        editor.clear().apply()
    }

    inline operator fun <reified T : Any> SharedPreferences.get(
        key: String,
        defaultValue: T? = null
    ): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
}

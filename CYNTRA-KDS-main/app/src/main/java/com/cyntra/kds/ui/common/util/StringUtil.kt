package com.cyntra.kds.ui.common.util

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.text.DecimalFormat

const val TAG = "StringUtil"

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun EditText.getString(): String {
    return this.text.toString().trim()

}
fun TextView.getString(): String {
    return this.text.toString().trim()
}

fun String.getString(): String {
    return this.trim()
}

fun formatDoublePriceString(input: Double): String {
    val df1 = DecimalFormat("0.00")
    return df1.format(("" + input).toDouble())
}

fun String.removeSpaces() =
    replace(" ", "")

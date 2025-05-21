package com.cyntra.kds.ui.common.util

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.cyntra.kds.R
import com.cyntra.kds.constants.FROM
import com.cyntra.kds.constants.ITEM_ADDED
import com.cyntra.kds.constants.ITEM_NAME
import com.cyntra.kds.constants.network.IMAGE_URL
import com.cyntra.kds.ui.internet.NoInternetActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.DecimalFormat

fun EditText.isNotEmpty(): Boolean {
    return this.text.toString().trim().isNotEmpty()

}

fun roundValue(input: Double): Double {
    return formatAmountToFourPlaces(roundHalfUp(input))
}

fun roundHalfUp(d: Double): Double {
    return d
}

fun formatAmountToFourPlaces(input: Double): Double {
    val df1 = DecimalFormat("0.0000")
    return df1.format(input).toDouble()
}

fun formatDoublePrice(input: Double): Double {
    val df1 = DecimalFormat("0.00")
    return df1.format(("" + input).toDouble()).toDouble()
}

fun convertToJson(input: Any): String {
    return Gson().toJson(input)
}

fun TextView.typeWrite(lifecycleOwner: LifecycleOwner, text: String, intervalMs: Long = 0) {
    this@typeWrite.text = ""
    lifecycleOwner.lifecycleScope.launch {
        this@typeWrite.text = text

//        repeat(text.length) {
//            //delay(intervalMs)
//            this@typeWrite.text = text.take(it + 1)
//        }
    }
}

fun Activity.flyToCart(imageView: ImageView, item: LinearLayout, onAnimationEnd: () -> Unit) {
    CircleAnimationUtil().attachActivity(this).setTargetView(imageView).setMoveDuration(1500)
        .setDestView(item).setAnimationListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                onAnimationEnd()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        }).startAnimation()
}

fun AppCompatActivity.isInternetConnectionAvailable() {
    kotlin.runCatching {
        observeConnectivityAsFlow()
            .distinctUntilChanged()
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    ConnectionState.Available -> {
                        delay(500)
                        finish()
                    }

                    ConnectionState.Unavailable -> {
                    }
                }
            }.catch { Log.e(TAG, "isInternetConnectionAvailble: ${it.message}") }
            .launchIn(lifecycleScope)
    }.onFailure {
        Log.e(TAG, "isInternetConnectionAvailble: ${it.message}")
    }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun AppCompatActivity.isNoInternetConnection() {
    kotlin.runCatching {
        observeConnectivityAsFlow()
            .distinctUntilChanged()
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    ConnectionState.Available -> {
                        delay(500)
                    }

                    ConnectionState.Unavailable -> {
                        startActivity(Intent(this, NoInternetActivity::class.java))
                    }
                }
            }.catch { Log.e(TAG, "isNoInternetConnection: ${it.message}") }.launchIn(lifecycleScope)
    }.onFailure {
        Log.e(TAG, "isNoInternetConnection: ${it.message}")
    }
}

fun Activity.moveToScreenWithFinish(activityName: Class<*>, fromWhere: String?=null) {
    startActivity(Intent(this, activityName).putExtra(FROM, fromWhere)).apply {
        finish()
    }
}


fun Activity.moveToScreenWithFinishWithItemName(
    activityName: Class<*>,
    fromWhere: String,
    itemName: String,
    isAdded: Boolean = false
) {
    startActivity(
        Intent(this, activityName).putExtra(FROM, fromWhere)
            .putExtra(ITEM_NAME, itemName)
            .putExtra(ITEM_ADDED, isAdded)
    ).apply {
        finish()
    }
}

fun Activity.moveToScreenWithFinish(
    activityName: Class<*>,
    fromWhere: String,
    phoneNumber: String
) {
    startActivity(
        Intent(this, activityName).putExtra("FROM", fromWhere)
            .putExtra("contactNumber", phoneNumber)
    ).apply {
        finish()
    }
}

fun Activity.moveToScreen(activityName: Class<*>, fromWhere: String? = null) {
    startActivity(Intent(this, activityName).putExtra("FROM", fromWhere))
}

fun Activity.restartActivity() {
    startActivity(
        Intent(this, this::class.java)
    ).apply {
        finish()
    }
}

fun buttonShrinkInAndOutAnim(view: Button) {
    view
        .animate()
        .translationYBy(0f)
        .scaleX(1.1f)
        .scaleY(1.1f)
        .setDuration(300)
        .alpha(1f)
        .withEndAction {
            view
                .animate()
                .setDuration(300)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1f)
                .withEndAction {
                    buttonShrinkInAndOutAnim(view)
                }.start()
        }.start()
}


fun ImageView.load(imageUrl : String) {
    Picasso.get()
        .load(IMAGE_URL+imageUrl)
        .into(this)
}

fun ImageView.loadWithPlaceHolder(imageUrl : String) {
    Picasso.get()
        .load(IMAGE_URL+imageUrl)
        .placeholder(R.drawable.icon_no_item)
        .error(R.drawable.icon_no_item)
        .into(this)
}

fun getBitmap(imageUrl : String) : Bitmap {
    return Picasso.get().load(IMAGE_URL+imageUrl).get()
}

fun riseAnimation(imageView: ImageView) {
    imageView
        .animate()
        .translationYBy(0f)
        .scaleX(0.9f)
        .scaleY(0.9f)
        .setDuration(300)
        .alpha(1f)
        .withEndAction {
            imageView
                .animate()
                .setDuration(300)
                .scaleX(0.75f)
                .scaleY(0.75f)
                .alpha(1f)
                .withEndAction {
                    imageView
                        .animate()
                        .setDuration(300)
                        .scaleX(0.6f)
                        .scaleY(0.6f)
                        .alpha(1f)
                        .withEndAction {
                            riseAnimation(imageView)
                        }
                        .start()
                }
                .start()
        }
        .start()
}



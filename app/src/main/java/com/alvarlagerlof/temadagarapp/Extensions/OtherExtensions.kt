package com.alvarlagerlof.koda.Extensions

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager



/**
 * Created by alvar on 2017-06-10.
 */


// Shared preferences
inline fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}



// Menu
fun Menu.show(menuId: Int) {
    val item = this.findItem(menuId)
    if (item != null) {
        item.isVisible = true
    }
}

fun Menu.hide(menuId: Int) {
    val item = this.findItem(menuId)
    if (item != null) {
        item.isVisible = false

    }
}


// Keyboard
fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    this.requestFocus()
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}


// Connectivity
fun Context.isConnected(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
}


// Int
fun Int.constrain(min: Int, max: Int) = if (this <= min) min else if (this >= max) max else this

fun Int.even() = this % 2 == 0
fun Int.odd() = this % 2 != 0

// Float
fun Int.dp(context: Context) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()
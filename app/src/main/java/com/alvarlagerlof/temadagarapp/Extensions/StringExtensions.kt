package com.alvarlagerlof.koda.Extensions

import android.util.Base64
import com.google.firebase.crash.FirebaseCrash
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by alvar on 2017-06-11.
 */

// Base64
fun String.base64Encode(): String {

    if (this == "") {
        return ""
    } else {
        var data: ByteArray? = null
        try {
            data = this.toByteArray(charset("UTF-8"))
        } catch (e1: UnsupportedEncodingException) {
            e1.printStackTrace()
        }

        return Base64.encodeToString(data, Base64.DEFAULT)
    }
}


fun String.base64Decode(): String {

    if (this == "") {
        return ""
    } else {
        val data = Base64.decode(this, android.util.Base64.DEFAULT)

        var decoded = "Error"

        try {
            decoded = String(data)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return decoded
    }
}


fun String.timeStampToDate(): String {

    if (this != "null") {
        val dateCurrent = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)

        val dayDate = this.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val dayCurrentList = dateCurrent.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()


        Integer.parseInt(dayCurrentList[0])

        val start = Calendar.getInstance()
        val end = Calendar.getInstance()

        start.set(Integer.parseInt(dayCurrentList[0]),
                Integer.parseInt(dayCurrentList[1]),
                Integer.parseInt(dayCurrentList[2]))

        end.set(Integer.parseInt(dayDate[0]),
                Integer.parseInt(dayDate[1]),
                Integer.parseInt(dayDate[2]))

        val startDate = start.time
        val endDate = end.time
        val startTime = startDate.time
        val endTime = endDate.time
        val diffTime = endTime - startTime
        val diffDays = diffTime / (1000 * 60 * 60 * 24)

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var testDate: Date? = null
        try {
            testDate = sdf.parse(this)
        } catch (e: Exception) {
            e.printStackTrace()
            FirebaseCrash.report(e)
        }

        val formatter = SimpleDateFormat("EEE, d MMMM")
        var newFormat = formatter.format(testDate)


        newFormat = newFormat.substring(0, 1).toUpperCase() + newFormat.substring(1)


        var finalDate = newFormat

        if (this == dateCurrent) {
            finalDate = "Idag"
        }

        return finalDate

    } else {
        return "Error"
    }


}
package com.alvarlagerlof.temadagarapp.FCM

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import com.alvarlagerlof.temadagarapp.Vars
import com.google.firebase.crash.FirebaseCrash
import com.google.firebase.messaging.FirebaseMessaging


/**
 * Created by alvar on 2016-10-02.
 */

class FirebaseSubscriptionsOnUpdate(context: Context) {

    init {

        // Shared preferences
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()

        // Get pInfo
        var pInfo: PackageInfo? = null
        try {
            pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            FirebaseCrash.report(e)
        }

        // New fcm subscription on update
        val currentVersionCode = pInfo!!.versionCode
        val lastVersionCode = preferences.getInt("last_version_code", 0)

        if (lastVersionCode != currentVersionCode && lastVersionCode != 0) {
            // Unsubscribe to all
            var api_version_string = Vars.API_VERSION
            api_version_string = api_version_string.substring(1)

            val api_version_int = Integer.parseInt(api_version_string)

            for (i in 1..api_version_int) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("fcmTodayv" + i.toString())
                FirebaseMessaging.getInstance().unsubscribeFromTopic("fcmComingv" + i.toString())
                FirebaseMessaging.getInstance().unsubscribeFromTopic("fcmNewv" + i.toString())
            }

            // Get rid of old stuff
            FirebaseMessaging.getInstance().unsubscribeFromTopic("today")
            FirebaseMessaging.getInstance().unsubscribeFromTopic("coming")
            FirebaseMessaging.getInstance().unsubscribeFromTopic("new")


            // Subscirbe again
            val notificationsToday = preferences.getBoolean(Vars.NOTIFICATIONS_TODAY, false)
            val notificationsComing = preferences.getBoolean(Vars.NOTIFICATIONS_COMING, false)
            val notificationsNew = preferences.getBoolean(Vars.NOTIFICATIONS_NEW, false)

            if (notificationsToday) {
                //FirebaseMessaging.getInstance().subscribeToTopic(Vars.TOPIC_TODAY)
            }

            if (notificationsComing) {
                //FirebaseMessaging.getInstance().subscribeToTopic(Vars.TOPIC_COMING)
            }

            if (notificationsNew) {
                //FirebaseMessaging.getInstance().subscribeToTopic(Vars.TOPIC_NEW)
            }
        }

        // Save
        editor.putInt("last_version_code", currentVersionCode)
        editor.apply()
    }

}

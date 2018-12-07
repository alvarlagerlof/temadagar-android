package com.alvarlagerlof.temadagarapp.FCM

import android.util.Log
import com.alvarlagerlof.temadagarapp.Vars
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by alvar on 2016-08-14.
 */

class FirebaseInstanceIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {


        //Getting registration token
        val refreshedToken = FirebaseInstanceId.getInstance().token

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)


        // Subscribe to topics
        val preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this)

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

    companion object {

        private val TAG = "MyFirebaseIIDService"
    }

}

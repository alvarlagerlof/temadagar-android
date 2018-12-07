package com.alvarlagerlof.temadagarapp.Settings

import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.Preference
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.alvarlagerlof.temadagarapp.R
import com.alvarlagerlof.temadagarapp.Vars
import com.bumptech.glide.Glide
import com.google.firebase.crash.FirebaseCrash
import com.google.firebase.messaging.FirebaseMessaging


/**
 * Created by alvar on 2015-08-18.
 */
class PreferenceFragment : android.support.v7.preference.PreferenceFragmentCompat() {

    internal lateinit var context: Context
    internal lateinit var preferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.setBackgroundColor(0xFFFFFF)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.prefs)

        context = this.activity

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext())

        val dialogPreference = preferenceScreen.findPreference("versionInfo")
        dialogPreference.onPreferenceClickListener = android.support.v7.preference.Preference.OnPreferenceClickListener {
            val inflater = activity.layoutInflater
            val view = inflater.inflate(R.layout.about, null)

            val versionTextView = view.findViewById<View>(R.id.version) as TextView
            val imageView = view.findViewById<View>(R.id.icon) as ImageView


            var pInfo: PackageInfo? = null
            try {
                pInfo = activity.packageManager.getPackageInfo(activity.packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                FirebaseCrash.report(e)
            }

            var version = pInfo!!.versionName
            val versionCode = pInfo.versionCode
            version = "Version " + version + " (" + versionCode.toString() + ")"

            versionTextView.text = version

            Glide.with(getContext())
                    .load(R.mipmap.ic_launcher)
                    .into(imageView)

            val builder = AlertDialog.Builder(activity, R.style.alertDialog)
            builder.setView(view)
            builder.create()
            builder.show()
            true
        }


        val ratePreference = preferenceScreen.findPreference("rate")
        ratePreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val appPackageName = context.packageName // getPackageName() from Context or Activity object
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)))
            } catch (e: ActivityNotFoundException) {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)))
            }

            true
        }


        val notificationsToday = preferenceScreen.findPreference("notificationsToday")
        notificationsToday.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val state = preferences.getBoolean(Vars.NOTIFICATIONS_TODAY, false)
            if (state) {
                FirebaseMessaging.getInstance().subscribeToTopic(Vars.TOPIC_TODAY)
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Vars.TOPIC_TODAY)
            }
            true
        }

        val notificationsComing = preferenceScreen.findPreference("notificationsComing")
        notificationsComing.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val state = preferences.getBoolean(Vars.NOTIFICATIONS_COMING, false)
            if (state) {
                FirebaseMessaging.getInstance().subscribeToTopic(Vars.TOPIC_COMING)
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Vars.TOPIC_COMING)
            }
            true
        }

        val notificationsNew = preferenceScreen.findPreference("notificationsNew")
        notificationsNew.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val state = preferences.getBoolean(Vars.NOTIFICATIONS_NEW, false)
            if (state) {
                FirebaseMessaging.getInstance().subscribeToTopic(Vars.TOPIC_NEW)
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Vars.TOPIC_NEW)
            }
            true
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val typedValue = TypedValue()
            val theme = (activity as AppCompatActivity).theme
            theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true)
            val colorToolbar = typedValue.data

            val icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            val td = ActivityManager.TaskDescription("Inställningar", icon, colorToolbar)

            (activity as AppCompatActivity).setTaskDescription(td)
            icon.recycle()

        }


    }

    override fun onCreatePreferences(bundle: Bundle, s: String) {

    }

}



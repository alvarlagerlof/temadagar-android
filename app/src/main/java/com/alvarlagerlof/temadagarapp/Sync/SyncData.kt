package com.alvarlagerlof.temadagarapp.Sync

import android.content.Context
import com.alvarlagerlof.koda.Extensions.base64Decode
import com.alvarlagerlof.koda.Extensions.isConnected
import com.alvarlagerlof.temadagarapp.RealmObject.DateIdRealmObject
import com.alvarlagerlof.temadagarapp.RealmObject.DateRealmObject
import com.alvarlagerlof.temadagarapp.RealmObject.DayRealmObject
import com.alvarlagerlof.temadagarapp.Vars
import com.arasthel.asyncjob.AsyncJob
import io.realm.Realm
import io.realm.RealmList
import okhttp3.OkHttpClient
import okhttp3.Request
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.io.IOException



/**
 * Created by alvar on 2017-06-25.
 */

class SyncData(val context: Context) {


    init {
        if (context.isConnected()) {
            AsyncJob.doInBackground {
                syncDays()
                syncDates()
            }

        } else {
            // TODO: IF OFFLINE AND NEVER LOADED BEGORE
            EventBus.getDefault().post(SyncEvent(message = "offline"))
        }

    }


    private fun syncDays() {

        try {

            val realm = Realm.getDefaultInstance()


            // Send it
            val request = Request.Builder().url(Vars.URL_SYNC_DAYS).build()
            val response = OkHttpClient.Builder().build().newCall(request).execute()

            if (response.isSuccessful) {

                val responseString = response.body()?.string()
                if (responseString != null) {

                    val json = JSONObject(responseString)
                    val data = json.getJSONArray("data")

                    if (json.getString("status") == "ok") {

                        // Remove projects that exists on locally but not on the server
                        realm.where(DayRealmObject::class.java).findAll().forEach {
                            val hasFoundInJson = (0..data.length() - 1).any { i -> data.getJSONObject(i).getInt("id") == it.id }
                            if (!hasFoundInJson) {
                                realm.beginTransaction()
                                it.deleteFromRealm()
                                realm.commitTransaction()
                            }
                        }

                        // Loop over response
                        for (i in 0..data.length() - 1) {

                            // Get project
                            val dayItem = data.getJSONObject(i)

                            val day = DayRealmObject()
                            day.id = dayItem.getInt("id")
                            day.title = dayItem.getString("title").base64Decode()
                            day.description = dayItem.getString("description").base64Decode()
                            day.introduced = dayItem.getString("introduced")
                            day.international = dayItem.getBoolean("international")
                            day.website = dayItem.getString("website").base64Decode()
                            day.fun_fact = dayItem.getString("fun_fact").base64Decode()
                            day.popularity = dayItem.getInt("popularity")
                            day.added = dayItem.getString("added")

                            realm.use { _ ->
                                realm.executeTransaction { _ -> Realm.getDefaultInstance().copyToRealmOrUpdate(day) }
                            }

                        }

                    }


                }

                AsyncJob.doOnMainThread {
                    EventBus.getDefault().post(SyncEvent(message = "update"))
                }
            }

            response.body()!!.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    private fun syncDates() {

        try {

            val realm = Realm.getDefaultInstance()


            // Send it
            val request = Request.Builder().url(Vars.URL_SYNC_DATES).build()
            val response = OkHttpClient.Builder().build().newCall(request).execute()

            if (response.isSuccessful) {

                val responseString = response.body()?.string()
                if (responseString != null) {

                    val json = JSONObject(responseString)
                    val data = json.getJSONArray("data")

                    if (json.getString("status") == "ok") {

                        // Remove projects that exists on locally but not on the server
                        realm.where(DateRealmObject::class.java).findAll().forEach {
                            val hasFoundInJson = (0..data.length() - 1).any { i -> data.getJSONObject(i).getInt("id") == it.id }
                            if (!hasFoundInJson) {
                                realm.beginTransaction()
                                it.deleteFromRealm()
                                realm.commitTransaction()
                            }
                        }

                        // Loop over response
                        for (i in 0..data.length() - 1) {

                            // Get project
                            val dateItem = data.getJSONObject(i)

                            val date = DateRealmObject()
                            date.id = dateItem.getInt("id")
                            date.date = dateItem.getString("date")

                            val ids = RealmList<DateIdRealmObject>()
                            for (n in 0..dateItem.getJSONArray("day_ids").length() - 1) {
                                val data = DateIdRealmObject()
                                data.id = dateItem.getJSONArray("day_ids").getInt(n)
                                ids.add(data)
                            }
                            date.day_ids = ids

                            realm.use { _ ->
                                realm.executeTransaction { _ -> Realm.getDefaultInstance().copyToRealmOrUpdate(date) }
                            }

                        }

                    }


                }

                AsyncJob.doOnMainThread {
                    EventBus.getDefault().post(SyncEvent(message = "update"))
                }
            }

            response.body()!!.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }








}
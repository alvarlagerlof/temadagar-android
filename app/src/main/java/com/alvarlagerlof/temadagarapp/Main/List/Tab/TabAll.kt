package com.alvarlagerlof.temadagarapp.Main.List.Tab

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alvarlagerlof.koda.Extensions.inflate
import com.alvarlagerlof.temadagarapp.Main.List.Adapter.GridAdapter
import com.alvarlagerlof.temadagarapp.Main.List.Object.DayObject
import com.alvarlagerlof.temadagarapp.R
import com.alvarlagerlof.temadagarapp.RealmObject.DayRealmObject
import com.arasthel.asyncjob.AsyncJob
import io.realm.Realm
import kotlinx.android.synthetic.main.tab.*

/**
 * Created by alvar on 2017-06-25.
 */

class TabAll : Fragment() {

    lateinit var adapter: GridAdapter
    var data: ArrayList<Any> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.tab)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = GridAdapter(context, data)

        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = StaggeredGridLayoutManager(getColumns(context), 1)

        getData()

    }

    fun getData() {
        AsyncJob.doInBackground {
            Realm.getDefaultInstance().where(DayRealmObject::class.java).findAll().forEach {
                data.add(DayObject(it.id,
                        it.title,"datum",
                        it.description,
                        it.introduced,
                        it.international,
                        it.website,
                        it.fun_fact,
                        it.popularity,
                        "#FFFFFF"))
            }
            AsyncJob.doOnMainThread {
                adapter.notifyDataSetChanged()
            }
        }

    }




    fun getColumns(context: Context): Int {

        val configuration = context.resources.configuration
        val screenWidthDp = configuration.screenWidthDp //The current width of the available screen space, in dp units, corresponding to screen width resource qualifier.

        if (screenWidthDp >= 820) {
            return 4
        } else if (screenWidthDp >= 595) {
            return 3
        } else if (screenWidthDp >= 360) {
            return 2
        } else {
            return 1
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (recycler_view != null) {
            val layoutManager = StaggeredGridLayoutManager(getColumns(context), 1)
            recycler_view.layoutManager = layoutManager
        }
    }

}
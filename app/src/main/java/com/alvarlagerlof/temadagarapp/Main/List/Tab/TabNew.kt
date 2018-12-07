package com.alvarlagerlof.temadagarapp.Main.List.Tab

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alvarlagerlof.koda.Extensions.inflate
import com.alvarlagerlof.temadagarapp.Main.List.Adapter.ListAdapter
import com.alvarlagerlof.temadagarapp.R
import kotlinx.android.synthetic.main.tab.*

/**
 * Created by alvar on 2017-06-25.
 */

class TabNew : Fragment() {

    lateinit var adapter: ListAdapter
    var data: ArrayList<Any> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.tab)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ListAdapter(context, data)

        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(context)
    }


}
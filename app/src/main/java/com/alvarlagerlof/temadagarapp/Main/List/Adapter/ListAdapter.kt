package com.alvarlagerlof.temadagarapp.Main.List.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alvarlagerlof.temadagarapp.Main.List.Object.DayObject
import com.alvarlagerlof.temadagarapp.Main.List.Object.SetupObject
import com.alvarlagerlof.temadagarapp.Main.List.ViewHolder.ViewHolderList
import com.alvarlagerlof.temadagarapp.Main.List.ViewHolder.ViewHolderSetup
import com.alvarlagerlof.temadagarapp.R
import com.alvarlagerlof.temadagarapp.Vars
import com.bumptech.glide.Glide
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by alvar on 2017-06-25.
 */

class ListAdapter(private val context: Context,
                  private val data: ArrayList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)

        when (i) {
            TYPE_SETUP -> return ViewHolderSetup(inflater.inflate(R.layout.item_list, viewGroup, false))
            TYPE_ITEM -> return ViewHolderList(inflater.inflate(R.layout.item_setup, viewGroup, false))
        }

        throw RuntimeException("there is no type that matches the type $i + make sure your using types correctly")
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderList -> {
                val item = data[position] as DayObject

                // Set
                holder.title.text = item.title
                holder.date.text = item.date

                Glide.with(context)
                        .load(Vars.IMAGE_LIST + item.id)
                        .thumbnail(0.5f)
                        .into(holder.image)


                holder.itemView.onClick {
                    // TODO: ADD THIS
                }
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        when (data[position]) {
            is SetupObject -> return TYPE_SETUP
            is DayObject -> return TYPE_ITEM
            else -> { // Note the block
                throw RuntimeException("there is no type that matches the type $data[position] + make sure your using types correctly")
            }
        }
    }

    override fun getItemCount(): Int {
        return data.count()
    }


    companion object {
        val TYPE_SETUP = 0
        val TYPE_ITEM = 1
    }


}
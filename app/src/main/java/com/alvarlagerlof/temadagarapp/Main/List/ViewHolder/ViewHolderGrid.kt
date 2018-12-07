package com.alvarlagerlof.temadagarapp.Main.List.ViewHolder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.item_grid.view.*

/**
 * Created by alvar on 2017-06-25.
 */

public class ViewHolderGrid internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal var box: LinearLayout = itemView.box
    internal var card: CardView = itemView.card
    internal var image: ImageView = itemView.image
    internal var title: TextView = itemView.title
    internal var date: TextView = itemView.date
}

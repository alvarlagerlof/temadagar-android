package com.alvarlagerlof.temadagarapp.Main.List.Adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alvarlagerlof.temadagarapp.Blur
import com.alvarlagerlof.temadagarapp.Main.List.Object.DayObject
import com.alvarlagerlof.temadagarapp.Main.List.Object.SetupObject
import com.alvarlagerlof.temadagarapp.Main.List.ViewHolder.ViewHolderGrid
import com.alvarlagerlof.temadagarapp.Main.List.ViewHolder.ViewHolderSetup
import com.alvarlagerlof.temadagarapp.R
import com.alvarlagerlof.temadagarapp.Vars
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * Created by alvar on 2017-06-25.
 */

class GridAdapter(private val context: Context,
                  private val data: ArrayList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)

        when (i) {
            TYPE_SETUP -> return ViewHolderSetup(inflater.inflate(R.layout.item_setup, viewGroup, false))
            TYPE_ITEM -> return ViewHolderGrid(inflater.inflate(R.layout.item_grid, viewGroup, false))
        }

        throw RuntimeException("there is no type that matches the type $i + make sure your using types correctly")
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderGrid -> {
                val item = data[position] as DayObject

                // Set
                holder.title.text = item.title
                holder.date.text = item.date


                /*Glide.with(context)
                        .asBitmap()
                        .load(Vars.IMAGE_GRID_SMALL + item.id)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(bitmap: Bitmap, anim: GlideAnimation) {
                                holder.image.setImageBitmap(bitmap)

                                Glide.with(context)
                                        .asBitmap()
                                        .load(Vars.IMAGE_GRID + item.id)
                                        .into(object : SimpleTarget<Bitmap>() {
                                            override fun onResourceReady(bitmap: Bitmap, anim: GlideAnimation) {
                                                holder.image.setImageBitmap(bitmap)
                                            }
                                        })
                            }
                        })

               Glide.with(context)
                       .load(Vars.IMAGE_GRID_SMALL + item.id)
                       .bitmapTransform(BlurTransformation(context, 25))
                       .listener(GlidePalette.with(Vars.IMAGE_GRID + item.id)
                                .use(BitmapPalette.Profile.VIBRANT_DARK)
                                .intoBackground(holder.card)
                                .intoBackground(holder.box)
                                .intoTextColor(holder.title, BitmapPalette.Swatch.TITLE_TEXT_COLOR)
                                .intoTextColor(holder.date, BitmapPalette.Swatch.BODY_TEXT_COLOR)
                                .crossfade(true)
                       )
                       .into(holder.image)*/




                val blurTransformation = object : Transformation {
                    override fun transform(source: Bitmap): Bitmap? {
                        val blurred = Blur.fastblur(holder.image.context, source, 10)
                        source.recycle()
                        return blurred
                    }

                    override fun key(): String {
                        return "blur()"
                    }
                }


                Picasso.with(context)
                        .load(Vars.IMAGE_GRID_SMALL + item.id) // thumbnail url goes here
                        .placeholder(R.raw.day_holder)
                        .transform(blurTransformation)
                        //.resize(imageViewWidth, imageViewHeight)
                        .into(holder.image, object : Callback {
                            override fun onSuccess() {
                                Picasso.with(context)
                                        .load(Vars.IMAGE_GRID + item.id)
                                        .into(object: com.squareup.picasso.Target {
                                            override fun onBitmapFailed(errorDrawable: Drawable?) {
                                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                            }

                                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                            }

                                            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                                                holder.image.setImageBitmap(bitmap)
                                            }
                                        })

                            }

                            override fun onError() {}
                        })



                holder.card.setCardBackgroundColor(Color.parseColor(item.color))
                holder.box.setBackgroundColor(Color.parseColor(item.color))


                holder.itemView.onClick {
                    // TODO: ADD THIS
                }
            }
            is ViewHolderSetup -> {
                val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                layoutParams.isFullSpan = true
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
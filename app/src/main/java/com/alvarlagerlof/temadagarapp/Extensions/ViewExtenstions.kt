package com.alvarlagerlof.koda.Extensions

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.LinearLayout

/**
 * Created by alvar on 2017-06-11.
 */




// General
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun View.replaceFragment(fragmentManager: FragmentManager, fragment: Fragment) {
    fragmentManager
            .beginTransaction()
            .replace(this.id, fragment)
            .addToBackStack(null)
            .commit()
}




// Animations
fun View.animateFadeIn() {

    val view = this
    val fadeIn = AlphaAnimation(0f, 1f)
    fadeIn.interpolator = DecelerateInterpolator() //projects_add this
    fadeIn.duration = 300
    fadeIn.fillAfter = true
    fadeIn.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            view.visibility = View.VISIBLE
        }

        override fun onAnimationEnd(animation: Animation) {

        }

        override fun onAnimationRepeat(animation: Animation) {

        }
    })

    view.startAnimation(fadeIn)


}

fun View.animateFadeOut() {

    val view = this
    val fadeOut = AlphaAnimation(1f, 0f)
    fadeOut.interpolator = AccelerateInterpolator() //and this
    fadeOut.duration = 300
    fadeOut.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {

        }

        override fun onAnimationEnd(animation: Animation) {
            view.visibility = View.GONE
        }

        override fun onAnimationRepeat(animation: Animation) {

        }
    })

    view.startAnimation(fadeOut)

}

fun View.animateExpand() {
    val view = this

    view.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    val targtetHeight = view.measuredHeight

    view.layoutParams.height = 0
    view.visibility = View.VISIBLE
    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            view.layoutParams.height = if (interpolatedTime == 1f)
                LinearLayout.LayoutParams.WRAP_CONTENT
            else
                (targtetHeight * interpolatedTime).toInt()
            view.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    a.duration = (targtetHeight / view.context.resources.displayMetrics.density).toInt().toLong()
    view.startAnimation(a)
}

fun View.animateCollapse() {

    val view = this
    val initialHeight = view.measuredHeight

    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            if (interpolatedTime == 1f) {
                view.visibility = View.GONE
            } else {
                view.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                view.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // 1dp/ms
    a.duration = (initialHeight / view.context.resources.displayMetrics.density).toInt().toLong()
    view.startAnimation(a)
}
package com.alvarlagerlof.temadagarapp.Feedback

import android.app.Dialog
import android.content.Context
import android.support.design.widget.*
import android.support.v7.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.alvarlagerlof.temadagarapp.R
import com.alvarlagerlof.temadagarapp.Vars
import com.google.firebase.crash.FirebaseCrash
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.support.v4.toast
import java.io.IOException

/**
 * Created by alvar on 2016-06-28.
 */
class Fix : BottomSheetDialogFragment() {

    internal lateinit var context: Context
    internal lateinit var type: String

    internal lateinit var cancelButton: Button
    internal lateinit var submitButton: Button

    internal lateinit var feedbackEditText: TextInputEditText
    internal lateinit var emailEditText: TextInputEditText

    internal lateinit var emailTextInputLayout: TextInputLayout
    internal lateinit var feedbackTextInputLayout: TextInputLayout

    internal lateinit var text: TextView


    fun passData(type: String) {
        this.type = type
    }

    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }

        }


        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            if (slideOffset < -0.15f) {
                dismiss()
            }
        }
    }


    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(getContext(), R.layout.sheet_feedback, null)
        dialog.setContentView(contentView)

        val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
        }

        cancelButton = contentView.findViewById<View>(R.id.cancel_button) as Button
        submitButton = contentView.findViewById<View>(R.id.submit_button) as Button

        feedbackEditText = contentView.findViewById<View>(R.id.feedback_edittext) as TextInputEditText
        feedbackTextInputLayout = contentView.findViewById<View>(R.id.feedback_textinputlayout) as TextInputLayout

        emailEditText = contentView.findViewById<View>(R.id.email_edittext) as TextInputEditText
        emailTextInputLayout = contentView.findViewById<View>(R.id.email_textinputlayout) as TextInputLayout

        text = contentView.findViewById<View>(R.id.text) as TextView

        context = feedbackEditText.context


        if (type.contains("DayInfo")) {
            text.text = "Har du hittat ett stavfel eller inkorrekt information i texten? Skiv ned det här det här"
        }

        // Set default text
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()

        emailEditText.setText(prefs.getString("email", ""))

        // Click listeners
        cancelButton.setOnClickListener { dismiss() }

        submitButton.setOnClickListener {
            feedbackTextInputLayout.isErrorEnabled = false
            emailTextInputLayout.isErrorEnabled = false

            feedbackEditText.clearFocus()
            emailEditText.clearFocus()

            if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches() && feedbackEditText.text.toString() != "" || emailEditText.text.toString() == "" && feedbackEditText.text.toString() != "") {
                dismiss()
                val url = Vars.URL_FEEDBACK_FIX + "?text=" + type + ": " + feedbackEditText.text + "&email=" + emailEditText.text

                try {
                    val request = Request.Builder()
                            .url(url)
                            .build()

                    val okHttpClient = OkHttpClient()

                    okHttpClient.newCall(request).execute()
                } catch (e: IOException) {
                    if (type.contains("DayInfo")) {
                        toast("Något gick snett")
                    } else {
                        toast("Något gick snett")
                    }
                    FirebaseCrash.report(e)
                }

                if (type.contains("DayInfo")) {
                    toast("Tack för ditt förslag!")
                } else {
                    toast("Tack för ditt förslag!")
                }
                editor.putString("email", emailEditText.text.toString()).apply()
            } else {

                if (feedbackEditText.text.toString() == "") {
                    feedbackTextInputLayout.error = "Fältet kan inte vara tomt"
                    feedbackEditText.requestFocus()
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches() && emailEditText.text.toString() != "") {
                    emailTextInputLayout.error = "Ange en giltig e-postadress"
                    emailEditText.requestFocus()
                }

            }
        }
    }
}
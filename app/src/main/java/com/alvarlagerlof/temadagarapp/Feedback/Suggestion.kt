package com.alvarlagerlof.temadagarapp.Feedback

/**
 * Created by alvar on 2017-06-25.
 */

import android.app.Dialog
import android.content.Context
import android.support.design.widget.*
import android.support.v7.preference.PreferenceManager
import android.view.View
import android.widget.Button
import com.alvarlagerlof.temadagarapp.R


/**
 * Created by alvar on 2016-06-28.
 */
class Suggestion : BottomSheetDialogFragment() {

    internal lateinit var context: Context
    internal var q: String? = null

    internal lateinit var cancelButton: Button
    internal lateinit var submitButton: Button

    internal lateinit var dayEditText: TextInputEditText
    internal lateinit var emailEditText: TextInputEditText

    internal lateinit var emailTextInputLayout: TextInputLayout
    internal lateinit var dayTextInputLayout: TextInputLayout


    fun passData(q: String) {
        this.q = q
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
        val contentView = View.inflate(getContext(), R.layout.sheet_add, null)
        dialog.setContentView(contentView)

        val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
        }

        cancelButton = contentView.findViewById<View>(R.id.cancel_button) as Button
        submitButton = contentView.findViewById<View>(R.id.submit_button) as Button

        dayEditText = contentView.findViewById<View>(R.id.day_edittext) as TextInputEditText
        dayTextInputLayout = contentView.findViewById<View>(R.id.day_textinputlayout) as TextInputLayout

        emailEditText = contentView.findViewById<View>(R.id.email_edittext) as TextInputEditText
        emailTextInputLayout = contentView.findViewById<View>(R.id.email_textinputlayout) as TextInputLayout

        context = dayEditText.context


        // Set default text
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()

        dayEditText.setText(q)
        emailEditText.setText(prefs.getString("email", ""))


        // Click listeners
        cancelButton.setOnClickListener { dismiss() }

        submitButton.setOnClickListener {
            dayTextInputLayout.isErrorEnabled = false
            emailTextInputLayout.isErrorEnabled = false

            dayEditText.clearFocus()
            emailEditText.clearFocus()

            /*if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches() && dayEditText.text.toString() != "" || emailEditText.text.toString() == "" && dayEditText.text.toString() != "") {
                dismiss()
                val url = DATA_SUGGESTION + "?title=" + dayEditText.text + "&email=" + emailEditText.text

                try {
                    val request = Request.Builder()
                            .url(url)
                            .build()

                    val okHttpClient = OkHttpClient()

                    okHttpClient.newCall(request).execute()
                } catch (e: IOException) {
                    if (q == null) {
                        MainActivity.snackbar(context, "Något gick snett")
                    } else {
                        Search.snackbar("Något gick snett")
                    }
                    FirebaseCrash.report(e)
                }

                if (q == null) {
                    MainActivity.snackbar(context, "Tack för ditt förslag!")
                } else {
                    Search.snackbar("Tack för ditt förslag!")
                }



                editor.putString("email", emailEditText.text.toString()).apply()
            } else {

                if (dayEditText.text.toString() == "") {
                    dayTextInputLayout.error = "Fältet kan inte vara tomt"
                    dayEditText.requestFocus()
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches() && emailEditText.text.toString() != "") {
                    emailTextInputLayout.error = "Ange en giltig e-postadress"
                    emailEditText.requestFocus()
                }

            }*/
        }


    }

}
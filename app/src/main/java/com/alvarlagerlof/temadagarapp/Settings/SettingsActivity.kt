package com.alvarlagerlof.temadagarapp.Settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.alvarlagerlof.temadagarapp.R
import kotlinx.android.synthetic.main.settings_activity.*

/**
 * Created by alvar on 2016-10-02.
 */

class SettingsActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        toolbar.title = "Inställningar"
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_dayinfo_close_white)




    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                overridePendingTransition(R.anim.stay, R.anim.slide_out)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

package com.alvarlagerlof.temadagarapp.Main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.alvarlagerlof.koda.Extensions.replaceFragment
import com.alvarlagerlof.temadagarapp.Main.List.Tab.TabAll
import com.alvarlagerlof.temadagarapp.Main.List.Tab.TabNew
import com.alvarlagerlof.temadagarapp.Main.List.Tab.TabPopular
import com.alvarlagerlof.temadagarapp.R
import com.alvarlagerlof.temadagarapp.Sync.SyncData
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    internal lateinit var fragment_new: TabNew
    internal lateinit var fragment_all: TabAll
    internal lateinit var fragment_popular: TabPopular

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Realm
        setupRealmMigration()

        // Sync
        SyncData(this)

        // Fix subscriptions
        //FirebaseSubscriptionsOnUpdate(this)


        // Init frags
        fragment_new = TabNew()
        fragment_all = TabAll()
        fragment_popular = TabPopular()

        // Views
        setSupportActionBar(toolbar)
        setUpBottomBar()




    }

    fun setupRealmMigration() {
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                //.schemaVersion(1)
                //.migration(MigrationRealm())
                .build())
    }


    fun setUpBottomBar() {
        fragment_container.replaceFragment(supportFragmentManager, fragment_all)
        toolbar.title = "Alla dagar"

        bottom_bar.setDefaultTab(R.id.tab_all)
        bottom_bar.setOnTabSelectListener({ tabId ->
            when (tabId) {
                R.id.tab_new -> {
                    fragment_container.replaceFragment(supportFragmentManager, fragment_new)
                    toolbar.title = "Senast tillagda"
                }
                R.id.tab_all -> {
                    fragment_container.replaceFragment(supportFragmentManager, fragment_all)
                    toolbar.title = "Alla dagar"
                }
                R.id.tab_popular -> {
                    fragment_container.replaceFragment(supportFragmentManager, fragment_popular)
                    toolbar.title = "Populära"
                }
            }
        })
    }





    // Save bottom_bar position
    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bottom_bar.onSaveInstanceState()
    }


    // Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

}

package com.alvarlagerlof.temadagarapp.RealmObject;

import io.realm.RealmObject;

/**
 * Created by alvar on 2017-06-25.
 */

public class DateIdRealmObject extends RealmObject {
    private int id;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
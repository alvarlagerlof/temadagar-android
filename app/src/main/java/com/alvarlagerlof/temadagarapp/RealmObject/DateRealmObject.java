package com.alvarlagerlof.temadagarapp.RealmObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alvar on 2017-06-25.
 */

public class DateRealmObject extends RealmObject {
    @PrimaryKey
    private int id;
    private String date;
    private RealmList<DateIdRealmObject> day_ids;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public RealmList<DateIdRealmObject> getDay_ids() { return day_ids; }
    public void setDay_ids(RealmList<DateIdRealmObject> day_ids) { this.day_ids = day_ids; }


}

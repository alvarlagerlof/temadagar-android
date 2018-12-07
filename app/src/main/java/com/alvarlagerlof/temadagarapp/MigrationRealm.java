package com.alvarlagerlof.temadagarapp;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

/**
 * Created by alvar on 2017-04-02.
 */

public class MigrationRealm implements io.realm.RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        // To add migrate, create a new if statement with the old version


        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();


        /*
        Migrate form version 0 (example)

       public class DateRealmObject extends RealmObject {
            private int id;
            private String date;
            private RealmList<DateId> day_ids;
            // getters and setters left out for brevity

            private class DateId extends RealmObject {
                private int id;
                // getters and setters left out for brevity
            }

        }

        public class DayRealmObject extends RealmObject {

            private int id;
            private String title;
            private String date;
            private String added;
            private String description;
            private String introduced;
            private Boolean international;
            private String website;
            private String fun_fact;
            private int popularity;
            private int type;
            // getters and setters left out for brevity

        }

        */


        if (oldVersion == 0) {
            /*
            schema.create("RequestQueueItem")
                    .addField("url", String.class);
                    ...
            */

            oldVersion++;
        }



    }


    @Override
    public int hashCode() {
        return 37;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof MigrationRealm);
    }
}

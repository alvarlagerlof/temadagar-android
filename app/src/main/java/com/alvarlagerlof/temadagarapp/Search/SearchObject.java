package com.alvarlagerlof.temadagarapp.Search;

/**
 * Created by alvar on 2015-07-10.
 */
public class SearchObject {
    public String id;
    public String title;
    public String date;
    public String description;
    public String introduced;
    public Boolean international;
    public String website;
    public String fun_fact;
    public String popularity;
    public String color;
    public static int type;

    public SearchObject(String id,
                        String title,
                        String date,
                        String description,
                        String introduced,
                        Boolean international,
                        String website,
                        String fun_fact,
                        String popularity,
                        String color,
                        int type) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.description = description;
        this.introduced = introduced;
        this.international = international;
        this.website = website;
        this.fun_fact = fun_fact;
        this.popularity = popularity;
        this.color = color;
        this.type = type;
    }
}
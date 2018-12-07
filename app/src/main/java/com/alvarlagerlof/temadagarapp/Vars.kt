package com.alvarlagerlof.temadagarapp

/**
 * Created by alvar on 2017-06-25.
 */

object Vars {

    val API_VERSION = "v8"


    // Data
    val URL_SYNC_DAYS = "https://api.temadagar.ravla.org/" + Vars.API_VERSION + "/data/get_days.php"
    val URL_SYNC_DATES = "https://api.temadagar.ravla.org/" + Vars.API_VERSION + "/data/get_dates.php"

    // Suggestions and Fixes
    val URL_FEEDBACK_FIX = "https://api.temadagar.ravla.org/" + Vars.API_VERSION + "/feedback_fix.php"
    val URL_FEEDBACK_SUGGESTION = "https://api.temadagar.ravla.org/" + Vars.API_VERSION + "/feedback_suggestion.php"

    // Popularity
    val URL_UPDATE_POPULARITY = "https://api.temadagar.ravla.org/" + Vars.API_VERSION + "/data/update_popularity.php?id="


    // Images
    private val IMAGE_TINY = "https://api.temadagar.ravla.org/" + Vars.API_VERSION + "/get_image.php?w=20&h=15&id="
    private val IMAGE_SMALL = "https://api.temadagar.ravla.org/" + Vars.API_VERSION + "/get_image.php?w=100&h=100&id="
    private val IMAGE_MEDIUM = "https://api.temadagar.ravla.org/" + Vars.API_VERSION + "/get_image.php?w=300&h=225&id="
    private val IMAGE_BIG = "https://api.temadagar.ravla.org/" + Vars.API_VERSION + "/get_image.php?w=901&h=675&id="

    val IMAGE_GRID = "https://api.temadagar.ravla.org/" + Vars.API_VERSION + "/get_image.php?w=2000&h=1125&id="
    val IMAGE_GRID_SMALL = IMAGE_TINY

    val IMAGE_LIST = IMAGE_SMALL

    val IMAGE_WIDGET = IMAGE_SMALL
    val IMAGE_DAYINFO = IMAGE_BIG
    val IMAGE_COLOR_GENERATION = IMAGE_SMALL
    val IMAGE_NOTIFICATION = IMAGE_BIG


    // FCM
    val NOTIFICATIONS_TODAY = "notificationsToday"
    val NOTIFICATIONS_COMING = "notificationsComing"
    val NOTIFICATIONS_NEW = "notificationsNew"

    val TOPIC_TODAY = "fcmToday" + Vars.API_VERSION
    val TOPIC_COMING = "fcmComing" + Vars.API_VERSION
    val TOPIC_NEW = "fcmNew" + Vars.API_VERSION


}
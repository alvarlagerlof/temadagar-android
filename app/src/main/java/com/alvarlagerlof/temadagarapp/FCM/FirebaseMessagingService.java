package com.alvarlagerlof.temadagarapp.FCM;

import android.content.Context;
import android.graphics.Bitmap;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static Context context = null;
    private static Bitmap icon = null;
/*
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            context = getApplicationContext();
            icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            Boolean todayNoti = preferences.getBoolean(NOTIFICATIONS_TODAY, false);
            Boolean comingNoti = preferences.getBoolean(NOTIFICATIONS_COMING, false);
            Boolean newNoti = preferences.getBoolean(NOTIFICATIONS_NEW, false);

            String type = remoteMessage.getData().get("type");

            String topic = remoteMessage.getFrom();

            if (topic.length() > 2) {
                String version = topic.substring(topic.length() - 2);
                if (version.equals(PrefValues.API_VERSION)) {
                    if (ConnectionUtils.isConnected(context)) {
                        if (remoteMessage.getFrom().startsWith("/topics/")) {
                            switch (type) {
                                case "todaySingle":
                                    if (todayNoti) {
                                        todayNotificationSingle(remoteMessage.getData().get("data"));
                                    } else {
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_TODAY);
                                    }
                                    break;
                                case "todayMultiple":
                                    if (todayNoti) {
                                        todayNotificationMultiple(remoteMessage.getData().get("data"));
                                    } else {
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_TODAY);
                                    }
                                    break;
                                case "coming":
                                    if (comingNoti) {
                                        comingNotification(remoteMessage.getData().get("data"));
                                    } else {
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_COMING);
                                    }
                                    break;
                                case "newSingle":
                                    if (newNoti) {
                                        newNotificationSingle(remoteMessage.getData().get("data"));
                                    } else {
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_NEW);
                                    }
                                    break;
                                case "newMultiple":
                                    if (newNoti) {
                                        newNotificationMultiple(remoteMessage.getData().get("data"));
                                    } else {
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_NEW);
                                    }
                                    break;
                            }
                        }
                    }
                }
            } else {
                // whatever is appropriate in this case
                throw new IllegalArgumentException("word has less than 2 characters!");
            }



        }

    }


    // Today
    public void todayNotificationSingle(final String data) {

        JSONObject object;
        String id = null;
        String date = null;
        String title = null;
        String introduced = null;
        Boolean international = null;
        String website = null;
        String popularity = null;
        String color = null;

        try {
            object = new JSONObject(data);
            id            = object.getString("id");
            date          = new DateStringConvertor().convert(object.getString("date"), false);
            title         = object.getString("title");
            introduced    = object.getString("introduced");
            international = object.getBoolean("international");
            website       = object.getString("website");
            popularity    = object.getString("popularity");
            color         = object.getString("color");

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }

        final String finalDate = date;
        final String finalId = id;
        final String finalTitle = title;
        final String finalIntroduced = introduced;
        final Boolean finalInternational = international;
        final String finalWebsite = website;
        final String finalPopularity = popularity;
        final String finalColor = color;

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap theBitmap = null;
                try {
                    Glide.with(FirebaseMessagingService.this)
                            .load(IMAGE_NOTIFICATION + finalId)
                            .into(theBitmap);
                     Glide.with(FirebaseMessagingService.this)
                            .load(IMAGE_NOTIFICATION + finalId)
                            .as()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(theBitmap)
                } catch (final ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                    FirebaseCrash.report(e);
                }
                return theBitmap;
            }
            @Override
            protected void onPostExecute(Bitmap bitmap) {

                final Bitmap[] bitmapSmall = {null};

                // Get small image
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        Bitmap theBitmap = null;
                        try {
                            theBitmap = Glide
                                    .with(FirebaseMessagingService.this)
                                    .load(IMAGE_INTENT + finalId)
                                    .asBitmap()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(-1,-1)
                                    .get();
                        } catch (final ExecutionException | InterruptedException e) {
                            Log.e(TAG, e.getMessage());
                            FirebaseCrash.report(e);
                        }
                        return theBitmap;
                    }
                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        bitmapSmall[0] = bitmap;
                    }
                }.execute();


                Intent intent = new Intent(FirebaseMessagingService.this, DayInfo.class);
                intent.putExtra("id", finalId);
                intent.putExtra("title", finalTitle);
                intent.putExtra("date", finalDate);
                intent.putExtra("description", "");
                intent.putExtra("fun_fact", "");
                intent.putExtra("introduced", finalIntroduced);
                intent.putExtra("international", finalInternational);
                intent.putExtra("website", finalWebsite);
                intent.putExtra("popularity", finalPopularity);
                intent.putExtra("color", finalColor);
                intent.putExtra("bitmap", bitmapSmall[0]);


                NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .setBigContentTitle("Temadagar")
                        .setSummaryText("Idag är det " + finalTitle);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setAutoCancel(true)
                        .setContentTitle("Temadagar")
                        .setContentText("Idag är det " + finalTitle)
                        .setLargeIcon(icon)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setStyle(style)
                        .setCategory(NotificationCompat.CATEGORY_EVENT)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                taskStackBuilder.addParentStack(DayInfo.class);
                taskStackBuilder.addNextIntent(intent);
                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(pendingIntent);

                Notification notification = builder.build();
                NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                manager.notify(1, notification);


            }
        }.execute();

    }

    public void todayNotificationMultiple(String data) {

        JSONArray array = null;
        String notificationText = "";
        PendingIntent pendingIntent = null;


        // Style
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                .setBigContentTitle("Idag är det");


        try {
            array = new JSONArray(data);

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                inboxStyle.addLine(object.getString("title"));
            }

            // Intent
            Intent intent = new Intent(context, MainActivity.class);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addParentStack(MainActivity.class);
            taskStackBuilder.addNextIntent(intent);
            pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


            if (array.length() == 2) {
                notificationText = array.getJSONObject(0).getString("title") + " och " + array.getJSONObject(1).getString("title");
            } else {
                for (int i = 0; i < array.length() - 1; i++) {
                    notificationText = notificationText + array.getJSONObject(i).getString("title") + ", ";
                }

                notificationText = notificationText.substring(0, notificationText.length() - 2);
                notificationText = notificationText + " och " + array.getJSONObject(array.length() - 1).getString("title");
            }

        } catch (JSONException e) {
            FirebaseCrash.report(e);
            e.printStackTrace();
        }

        // Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("Idag är det")
                .setContentText(notificationText)
                .setLargeIcon(icon)
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(inboxStyle)
                .setNumber(array.length())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Build
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1, notification);

    }


    // Coming
    public void comingNotification(String data) {

        JSONArray array = null;
        String notificationText = "";
        PendingIntent pendingIntent = null;


        // Style
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                .setBigContentTitle("Kommande temadagar");


        try {
            array = new JSONArray(data);

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                //inboxStyle.addLine(getFormattedString(object.getString("date"), object.getString("title")));
            }

            // Intent
            Intent intent = new Intent(context, MainActivity.class);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addParentStack(MainActivity.class);
            taskStackBuilder.addNextIntent(intent);
            pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


            if (array.length() == 2) {
                notificationText = array.getJSONObject(0).getString("title") + " och " + array.getJSONObject(1).getString("title");
            } else {
                for (int i = 0; i < array.length() - 1; i++) {
                    notificationText = notificationText + array.getJSONObject(i).getString("title") + ", ";
                }

                notificationText = notificationText.substring(0, notificationText.length() - 2);
                notificationText = notificationText + " och " + array.getJSONObject(array.length() - 1).getString("title");
            }

        } catch (JSONException e) {
            FirebaseCrash.report(e);
            e.printStackTrace();
        }

        // Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("Kommande temadagar")
                .setContentText(notificationText)
                .setLargeIcon(icon)
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(inboxStyle)
                .setNumber(array.length())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MIN);

        // Build
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(2, notification);


    }


    // New
    public void newNotificationSingle(final String data)  {

        JSONObject object;
        String id = null;
        String date = null;
        String title = null;
        String introduced = null;
        String international = null;
        String website = null;
        String popularity = null;
        String color = null;

        try {
            object = new JSONObject(data);
            id            = object.getString("id");
            date          = new DateStringConvertor().convert(object.getString("date"), false);
            title         = object.getString("title");
            introduced    = object.getString("introduced");
            international = object.getString("international");
            website       = object.getString("website");
            color         = object.getString("color");
            popularity    = object.getString("popularity");


        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }

        final String finalDate = date;
        final String finalId = id;
        final String finalTitle = title;
        final String finalIntroduced = introduced;
        final String finalInternational = international;
        final String finalWebsite = website;
        final String finalPopularity = popularity;
        final String finalColor = color;

       new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap theBitmap = null;
                try {
                    theBitmap = Glide
                            .with(FirebaseMessagingService.this)
                            .load(IMAGE_NOTIFICATION + finalId)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(-1,-1)
                            .get();
                } catch (final ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                    FirebaseCrash.report(e);
                }
                return theBitmap;
            }
            @Override
            protected void onPostExecute(Bitmap bitmap) {

                final Bitmap[] bitmapSmall = {null};

                // Get small image
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        Bitmap theBitmap = null;
                        try {
                            theBitmap = Glide
                                    .with(FirebaseMessagingService.this)
                                    .load(IMAGE_INTENT + finalId)
                                    .asBitmap()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(-1,-1)
                                    .get();
                        } catch (final ExecutionException | InterruptedException e) {
                            Log.e(TAG, e.getMessage());
                            FirebaseCrash.report(e);
                        }
                        return theBitmap;
                    }
                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        bitmapSmall[0] = bitmap;
                    }
                }.execute();



                Intent intent = new Intent(FirebaseMessagingService.this, DayInfo.class);
                intent.putExtra("id", finalId);
                intent.putExtra("title", finalTitle);
                intent.putExtra("date", finalDate);
                intent.putExtra("description", "");
                intent.putExtra("fun_fact", "");
                intent.putExtra("introduced", finalIntroduced);
                intent.putExtra("international", finalInternational);
                intent.putExtra("website", finalWebsite);
                intent.putExtra("popularity", finalPopularity);
                intent.putExtra("color", finalColor);
                //intent.putExtra("bitmap", bitmapSmall[0]);

                NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .setBigContentTitle("Ny temadag inlagd")
                        .setSummaryText(finalTitle);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setAutoCancel(true)
                        .setContentTitle("Ny temadag inlagd")
                        .setContentText(finalTitle)
                        .setLargeIcon(icon)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setStyle(style)
                        .setCategory(NotificationCompat.CATEGORY_EVENT)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                taskStackBuilder.addParentStack(DayInfo.class);
                taskStackBuilder.addNextIntent(intent);
                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(pendingIntent);

                Notification notification = builder.build();
                NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                manager.notify(3, notification);


            }
        }.execute();


    }

    public void newNotificationMultiple(String data) {

        JSONArray array = null;
        String notificationText = "";
        PendingIntent pendingIntent = null;


        // Style
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                .setBigContentTitle("Nya temadagar inlagda");


        try {
            array = new JSONArray(data);

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                inboxStyle.addLine(getFormattedString(object.getString("date"), object.getString("title")));
            }

            // Intent
            Intent intent = new Intent(context, MainActivity.class);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addParentStack(MainActivity.class);
            taskStackBuilder.addNextIntent(intent);
            pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


            if (array.length() == 2) {
                notificationText = array.getJSONObject(0).getString("title") + " och " + array.getJSONObject(1).getString("title");
            } else {
                for (int i = 0; i < array.length() - 1; i++) {
                    notificationText = notificationText + array.getJSONObject(i).getString("title") + ", ";
                }

                notificationText = notificationText.substring(0, notificationText.length() - 2);
                notificationText = notificationText + " och " + array.getJSONObject(array.length() - 1).getString("title");
            }

        } catch (JSONException e) {
            FirebaseCrash.report(e);
            e.printStackTrace();
        }

        // Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("Nya temadagar inlagda")
                .setContentText(notificationText)
                .setLargeIcon(icon)
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(inboxStyle)
                .setNumber(array.length())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Build
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(3, notification);
    }


    // Bold dates
    private SpannableString getFormattedString(String date, String title) {
        String boldContact = "<b>" + new DateStringConvertor().convert(date, false) + "</b>";
        return new SpannableString(Html.fromHtml(boldContact + " " + title));
    }*/
}

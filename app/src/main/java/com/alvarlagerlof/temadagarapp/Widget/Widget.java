package com.alvarlagerlof.temadagarapp.Widget;

/**
 * Created by alvar on 2015-08-02.
 */

import android.appwidget.AppWidgetProvider;



public class Widget extends AppWidgetProvider {


   /* public Bitmap getBitmap(Context context, String url) {

        try {
            URL urlBitmap = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlBitmap.openConnection();
            connection.setDoInput(true);
            connection.connect();

            Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            connection.disconnect();

            return bitmap;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeResource(context.getResources(), R.raw.sad);
    }





    public class getData extends AsyncTask<Void, Void, Void> {
        private Context context;
        private RemoteViews view;
        private AppWidgetManager appWidgetManager;
        private int currentWidgetId;

        getData(Context context, RemoteViews view, AppWidgetManager appWidgetManager, int currentWidgetId){
            this.context = context;
            this.view = view;
            this.appWidgetManager = appWidgetManager;
            this.currentWidgetId = currentWidgetId;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            // Set colors
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();

            int color = prefs.getInt("widget_color", 0);

            if (!prefs.contains("widget_color")) {
                color = 0xffffffff;
                editor.putInt("widget_color", 0xffffffff);
                editor.apply();
            } else {
                view.setInt(R.id.layout, "setBackgroundColor", color);
            }



            /*if (!new ColorExtraUtils().isColorLight(color)) {
                view.setViewVisibility(R.id.widget_more_number_white, View.VISIBLE);
                view.setViewVisibility(R.id.widget_title_text_white, View.VISIBLE);
                view.setViewVisibility(R.id.widget_today_text_white, View.VISIBLE);

                view.setViewVisibility(R.id.widget_more_number_black, View.GONE);
                view.setViewVisibility(R.id.widget_title_text_black, View.GONE);
                view.setViewVisibility(R.id.widget_today_text_black, View.GONE);
            }

            if (new ConnectionUtils().isConnected(context)) {

                try {

                    // Get the data
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(DATA_WIDGET)
                            .build();

                    Response response = client.newCall(request).execute();

                    if (!response.isSuccessful()) {
                        // Failed
                        view.setTextViewText(R.id.widget_title_text_black, "Kunde inte hämta data");
                        view.setTextViewText(R.id.widget_title_text_white, "Kunde inte hämta data");
                        view.setViewVisibility(R.id.widget_more_number_black, View.GONE);
                        view.setViewVisibility(R.id.widget_more_number_white, View.GONE);
                        view.setImageViewBitmap(R.id.widget_image, BitmapFactory.decodeResource(context.getResources(), R.raw.sad));

                        // On click
                        Intent intent = new Intent(context, MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                        view.setOnClickPendingIntent(R.id.layout, pendingIntent);

                    } else {
                        // Succeeded
                        JSONObject dataObject = new JSONObject(response.body().string());

                        if (dataObject.getBoolean("atLeastOneItem")) {

                            // Set title
                            view.setTextViewText(R.id.widget_title_text_white, dataObject.getString("title"));
                            view.setTextViewText(R.id.widget_title_text_black, dataObject.getString("title"));


                            // Get small image and set to view
                            Bitmap bitmapSmall = getBitmap(context, IMAGE_WIDGET + dataObject.getString("id"));
                            view.setImageViewBitmap(R.id.widget_image, bitmapSmall);

                            // If more than one, snow "+x"
                            if (dataObject.getInt("numberOfItemsMore") > 0) {
                                view.setTextViewText(R.id.widget_more_number_black, "+" + String.valueOf(dataObject.getInt("numberOfItemsMore")));
                                view.setTextViewText(R.id.widget_more_number_white, "+" + String.valueOf(dataObject.getInt("numberOfItemsMore")));
                            } else {
                                view.setViewVisibility(R.id.widget_more_number_black, View.GONE);
                                view.setViewVisibility(R.id.widget_more_number_white, View.GONE);
                            }


                            // Intent
                            // Get big image
                            Bitmap bitmapBig = getBitmap(context, IMAGE_INTENT + dataObject.getString("id"));



                            // On click
                            Intent intent = new Intent(context, DayInfo.class)
                                .putExtra("id", dataObject.getString("id"))
                                .putExtra("title", dataObject.getString("title"))
                                .putExtra("date", dataObject.getString("date"))
                                .putExtra("description", dataObject.getString("description"))
                                .putExtra("introduced", dataObject.getString("introduced"))
                                .putExtra("international", dataObject.getBoolean("international"))
                                .putExtra("website",dataObject.getString("website"))
                                .putExtra("fun_fact", dataObject.getString("fun_fact"))
                                .putExtra("popularity", dataObject.getString("popularity"))
                                .putExtra("color", dataObject.getString("color"))
                                .putExtra("bitmap", bitmapBig);

                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                            view.setOnClickPendingIntent(R.id.layout, pendingIntent);

                        } else {

                            // No special day today
                            view.setTextViewText(R.id.widget_title_text_black, "Ingen temadag");
                            view.setTextViewText(R.id.widget_title_text_white, "Ingen temadag");
                            view.setViewVisibility(R.id.widget_more_number_white, View.GONE);
                            view.setViewVisibility(R.id.widget_more_number_black, View.GONE);
                            view.setImageViewBitmap(R.id.widget_image, BitmapFactory.decodeResource(context.getResources(), R.raw.sad));

                            // On click
                            Intent intent = new Intent(context, MainActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                            view.setOnClickPendingIntent(R.id.layout, pendingIntent);

                        }
                    }

                } catch (IOException e){
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                } catch(JSONException e){
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                }


            } else {
                // Not connected
                view.setTextViewText(R.id.widget_title_text_white, "Kunde inte hämta data");
                view.setTextViewText(R.id.widget_title_text_black, "Kunde inte hämta data");

                view.setViewVisibility(R.id.widget_more_number_white, View.GONE);
                view.setViewVisibility(R.id.widget_more_number_black, View.GONE);
                view.setImageViewBitmap(R.id.widget_image, BitmapFactory.decodeResource(context.getResources(), R.raw.sad));

                // On click
                Intent intent = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                view.setOnClickPendingIntent(R.id.layout, pendingIntent);

            }

            appWidgetManager.updateAppWidget(currentWidgetId, view);

            return null;

        }

    }







    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        for (int i = 0; i < appWidgetIds.length; i++) {

            final RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            getData getData = new getData(context, view, appWidgetManager, appWidgetIds[i]);
            getData.execute();

        }


    }

    /*
    // This is to make an intent
    Intent day_intent = new Intent(context, DayInfo.class);
    day_intent.putExtra("id", id);
    day_intent.putExtra("date", date);
    day_intent.putExtra("title", title);
    day_intent.putExtra("isColorLight", isColorLight);
    day_intent.putExtra("bitmap", bitmap);
    PendingIntent dayPendingIntent = PendingIntent.getActivity(context, 0, day_intent, 0);
    views.setOnClickPendingIntent(R.id.layout, dayPendingIntent);
    views.setViewVisibility(R.id.widget_more_number, View.GONE);



    */

}
package com.alvarlagerlof.temadagarapp.Dayinfo;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alvarlagerlof.temadagarapp.R;

import java.io.InputStream;
import java.net.HttpURLConnection;


/**
 * Created by Alvar
 */
public class DayInfo extends AppCompatActivity {

    TextView dayIntroduced;
    TextView dayDescription;
    TextView dayInternational;
    TextView dayFunFact;
    TextView dayWebsite;

    TextView daySectionFacts;
    TextView daySectionDescription;
    TextView daySectionFunFact;
    TextView daySectionWebsite;

    ImageView dayImage;
    ImageView dayInternationalImg;

    CardView dayinfoContentFacts;
    CardView cardViewDidYouKnow;
    CardView cardViewWebsite;
    CardView cardViewDescription;

    CollapsingToolbarLayout collapsingToolbar;

    ProgressBar progressBarDescription;
    ProgressBar progressBarFunFact;

    public static CoordinatorLayout coordinatorLayout;

    AppBarLayout appbarLayout;
    Toolbar toolbar;

    // Toolbarbox
    CardView toolbarBox;
    TextView toolbar_title;
    TextView toolbar_date;

    String id;
    String title = null;
    String date = null;
    String description = null;
    String introduced = null;
    Boolean international = null;
    String website = null;
    String fun_fact = null;
    String popularity = null;
    Bitmap bitmap;
    int color;

    HttpURLConnection con;
    InputStream input;

    Intent shareIntent;

    Boolean hasLoadedFullSize = false;
    Boolean showFunFact = true;



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        StrictMode.enableDefaults();

        /*// Toolbar box
        toolbarBox = (CardView) findViewById(R.id.toolbar_box);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_date = (TextView) findViewById(R.id.toolbar_date);

        // Blocks
        dayinfoContentFacts = (CardView) findViewById(R.id.dayinfo_content1);
        cardViewDescription = (CardView) findViewById(R.id.dayinfo_content2);
        cardViewDidYouKnow = (CardView) findViewById(R.id.dayinfo_content3);
        cardViewWebsite = (CardView) findViewById(R.id.dayinfo_content4);

        // Just text
        dayImage = (ImageView) findViewById(R.id.info_more_image);
        //dayDate = (TextView) findViewById(R.id.info_more_date);
        dayIntroduced = (TextView) findViewById(R.id.info_more_introduced);
        dayDescription = (TextView) findViewById(R.id.info_more_description);
        dayInternational = (TextView) findViewById(R.id.info_more_international);
        dayFunFact = (TextView) findViewById(R.id.fun_fact);
        dayWebsite = (TextView) findViewById(R.id.website);

        // Images on top
        dayInternationalImg = (ImageView) findViewById(R.id.info_more_international_img);

        // Small titles
        daySectionFacts = (TextView) findViewById(R.id.dayinfo_section_text_facts);
        daySectionDescription = (TextView) findViewById(R.id.dayinfo_section_text_description);
        daySectionFunFact = (TextView) findViewById(R.id.dayinfo_section_text_fun_fact);
        daySectionWebsite = (TextView) findViewById(R.id.dayinfo_section_text_website);

        // Other
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);

        appbarLayout = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        progressBarDescription = (ProgressBar) findViewById(R.id.progress_bar_description);
        progressBarFunFact = (ProgressBar) findViewById(R.id.progress_bar_fun_fact);




        // From intent
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            SharedPreferences day_id = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
            id = day_id.getString("id", "null");
            title = day_id.getString("title", "Laddar");
            date = day_id.getString("date", "Laddar");
            description = day_id.getString("title", "Laddar");
            introduced = day_id.getString("introduced", "Laddar");
            international = day_id.getBoolean("international", false);
            website = day_id.getString("website", "Laddar");
            fun_fact = day_id.getString("fun_fact", "Laddar");
            popularity = day_id.getString("popularity", "Laddar");
            color = day_id.getInt("color", 0x000000);
        } else {
            id = extras.getString("id");
            title = extras.getString("title");
            date = extras.getString("date");
            description = extras.getString("description");
            introduced = extras.getString("introduced");
            international = extras.getBoolean("international");
            website = extras.getString("website");
            fun_fact = extras.getString("fun_fact");
            popularity = extras.getString("popularity");
            try {
                color = Color.parseColor(extras.getString("color"));
            } catch (Exception ex) {
                ex.printStackTrace();
                FirebaseCrash.report(ex);
                color = 0xff000000;
            }
        }

        Intent intent = getIntent();
        bitmap = intent.getParcelableExtra("bitmap");
        dayImage.setImageBitmap((Bitmap) intent.getParcelableExtra("bitmap"));

        // Save for onResume
        SharedPreferences day_id = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = day_id.edit();
        editor.putString("id", id);
        editor.putString("title", title);
        editor.putString("description", description);
        editor.putString("introduced", introduced);
        editor.putBoolean("international", international);
        editor.putString("website", website);
        editor.putString("fun_fact", fun_fact);
        editor.putString("popularity", popularity);
        editor.putInt("color", color);
        editor.apply();

       // setToolbar();

        if (description == null) {
            description = "";
        }
        if (fun_fact == null) {
            fun_fact = "";
        }

        // Fix and hide
        if (introduced.startsWith("O")) {
            introduced = "Sedan: Okänt";
        } else {
            introduced = "Sedan " + introduced;
        }
        if (website.contains("wiki")) {
            daySectionWebsite.setText("Wikipedia");
        }
        if (website.equals("")) {
            cardViewWebsite.setVisibility(View.GONE);
        }
        if (fun_fact.equals("")) {
            cardViewDidYouKnow.setVisibility(View.GONE);
            showFunFact = false;
        }
        if (international) {
            dayInternational.setText("Internationell");
        } else {
            dayInternational.setText("Inte internationell");
        }

        if (description.equals("")) {
            progressBarDescription.setVisibility(View.VISIBLE);
            progressBarFunFact.setVisibility(View.VISIBLE);

            dayDescription.setVisibility(View.GONE);
            dayFunFact.setVisibility(View.VISIBLE);

            loadDescriptionAndFunFact loadDescriptionAndFunFact = new loadDescriptionAndFunFact();
            loadDescriptionAndFunFact.execute();
        }

        // Set
        dayIntroduced.setText(introduced);
        dayDescription.setText(description);
        dayFunFact.setText(fun_fact);
        dayWebsite.setText(website);

        if (international) {
            dayInternationalImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_dayinfo_not_international));
        }

        toolbar_title.setText(title);
        toolbar_date.setText(date);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                loadBigImage loadBigImage = new loadBigImage();
                loadBigImage.execute(id);
            }
        }, 1000);


        // Lollipop and up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            final Transition transition = DayInfo.this.getWindow().getSharedElementEnterTransition();

            if (transition != null) {
                transition.addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionEnd(Transition transition) {
                        toolbar.setVisibility(View.VISIBLE);
                        Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_top);
                        toolbar.startAnimation(slideIn);

                        dayinfoContentFacts.setVisibility(View.VISIBLE);
                        cardViewDescription.setVisibility(View.VISIBLE);
                        cardViewWebsite.setVisibility(View.VISIBLE);

                        if (!fun_fact.equals("")) {
                            cardViewDidYouKnow.setVisibility(View.VISIBLE);
                        }

                        dayinfoContentFacts.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.dayinfo_content_1));
                        cardViewDescription.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.dayinfo_content_2));
                        cardViewDidYouKnow.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.dayinfo_content_3));
                        cardViewWebsite.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.dayinfo_content_4));

                        transition.removeListener(this);
                    }

                    @Override
                    public void onTransitionStart(Transition transition) {
                        toolbar.setVisibility(View.INVISIBLE);
                        dayinfoContentFacts.setVisibility(View.INVISIBLE);
                        cardViewDescription.setVisibility(View.INVISIBLE);
                        cardViewDidYouKnow.setVisibility(View.GONE);
                        cardViewWebsite.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {
                        transition.removeListener(this);
                    }

                    @Override
                    public void onTransitionPause(Transition transition) {}

                    @Override
                    public void onTransitionResume(Transition transition) {}
                });
            }

        } else {

            dayinfoContentFacts.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dayinfo_content_1));
            cardViewDescription.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dayinfo_content_2));
            cardViewDidYouKnow.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dayinfo_content_3));
            cardViewWebsite.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dayinfo_content_4));

        }

        updatePopularityCounter updatePopularityCounter = new updatePopularityCounter();
        updatePopularityCounter.execute(id);*/

    }




   /* class loadBigImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {

                /*String url = IMAGE_DAYINFO + strings[0];

                Bitmap bitmapPre = null;

                try {
                    /*bitmapPre = Glide
                            .with(DayInfo.this)
                            .load(String.valueOf(url))
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(-1, -1)
                            .get();
                } catch (final ExecutionException e) {
                    FirebaseCrash.report(e);

                } catch (final InterruptedException e) {
                    Log.e("Ops", e.getMessage());
                    FirebaseCrash.report(e);
                }

                return bitmapPre;

            } finally {
            }
        }

        protected void onPostExecute(Bitmap bitmapfinal) {
            if (bitmapfinal != null) {
                dayImage.setImageBitmap(bitmapfinal);
                bitmap = bitmapfinal;
                hasLoadedFullSize = true;
            }

        }
    }

    class sendColor extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // Send it to the server
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(params[0])
                    .build();
            try {
                client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            return null;
        }
    }

    class loadDescriptionAndFunFact extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(DATA_DESCRIPTION_AND_FUN_FACT + id)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }

            response.close();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String description = null;
            String fun_fact = null;

            if (s != null) {
                try {
                    description = new JSONObject(s).getString("description");
                    fun_fact = new JSONObject(s).getString("fun_fact");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (description.equals(null) || fun_fact.equals(null)) {
                description = "Texten kunde inte laddas";
            }

            if (fun_fact.equals(null) || fun_fact.equals("")) {
                cardViewDidYouKnow.setVisibility(View.GONE);
            } else {
                cardViewDidYouKnow.setVisibility(View.VISIBLE);
            }

            dayDescription.setText(description);
            dayFunFact.setText(fun_fact);

            dayDescription.setVisibility(View.VISIBLE);
            dayFunFact.setVisibility(View.VISIBLE);

            progressBarDescription.setVisibility(View.GONE);
            progressBarFunFact.setVisibility(View.GONE);
        }
    }

    class updatePopularityCounter extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URL_UPDATE_POPULARITY + params[0])
                    .build();
            try {
                Response response = client.newCall(request).execute();
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            return null;
        }
    }


    public void setToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context context = this;


        if (String.format("#%06X", (0xFFFFFF & color)).equals("#000000")) {
            if (bitmap != null) {

                Palette.from(bitmap)
                        .clearFilters()
                        .generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                if (palette != null) {

                                    if (palette.getVibrantColor(0x000000) != 0x000000) {
                                        color = palette.getVibrantColor(0x000000);

                                    } else if (palette.getDarkVibrantColor(0x000000) != 0x000000) {
                                        color = palette.getDarkVibrantColor(0x000000);

                                    } else if (palette.getLightVibrantColor(0x000000) != 0x000000) {
                                        color = palette.getLightVibrantColor(0x000000);

                                    } else if (palette.getMutedColor(0x000000) != 0x000000) {
                                        color = palette.getMutedColor(0x000000);

                                    } else if (palette.getDarkMutedColor(0x000000) != 0x000000) {
                                        color = palette.getDarkMutedColor(0x000000);

                                    } else if (palette.getLightMutedColor(0x000000) != 0x000000) {
                                        color = palette.getLightMutedColor(0x000000);
                                    }


                                    if (new ColorExtraUtils().isColorLight(color)) {
                                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dayinfo_close_black);

                                        toolbar_title.setTextColor(ContextCompat.getColor(context, R.color.black));
                                        toolbar_date.setTextColor(ContextCompat.getColor(context, R.color.black));
                                    } else {
                                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dayinfo_close_white);

                                        toolbar_title.setTextColor(ContextCompat.getColor(context, R.color.white));
                                        toolbar_date.setTextColor(ContextCompat.getColor(context, R.color.white));

                                        collapsingToolbar.setExpandedTitleColor(0xffffffff);
                                        collapsingToolbar.setCollapsedTitleTextColor(0xffffffff);
                                    }

                                    collapsingToolbar.setScrimsShown(true);
                                    collapsingToolbar.setContentScrimColor(color);
                                    daySectionFacts.setTextColor(color);
                                    daySectionDescription.setTextColor(color);
                                    daySectionFunFact.setTextColor(color);
                                    daySectionWebsite.setTextColor(color);
                                    toolbarBox.setCardBackgroundColor(color);

                                    String stringColor = String.format("#%06X", (0xFFFFFF & color));
                                    stringColor = stringColor.substring(1);
                                    String url = DATA_SEND_COLOR + "?id=" + id + "&isColorLight=" + stringColor;

                                    sendColor sendColor = new sendColor();
                                    sendColor.execute(url);

                                    // Lollipop and up
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                                        ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(title, icon, color);
                                        setTaskDescription(td);
                                        icon.recycle();
                                    }


                                } else {
                                    Log.e("colors", "no palette");
                                }
                            }
                        });
            } else {
                if (new ColorExtraUtils().isColorLight(color)) {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dayinfo_close_black);

                    toolbar_title.setTextColor(ContextCompat.getColor(context, R.color.black));
                    toolbar_date.setTextColor(ContextCompat.getColor(context, R.color.black));
                } else {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dayinfo_close_white);

                    toolbar_title.setTextColor(ContextCompat.getColor(context, R.color.white));
                    toolbar_date.setTextColor(ContextCompat.getColor(context, R.color.white));

                    collapsingToolbar.setExpandedTitleColor(0xffffffff);
                    collapsingToolbar.setCollapsedTitleTextColor(0xffffffff);
                }

                collapsingToolbar.setScrimsShown(true);
                collapsingToolbar.setContentScrimColor(color);
                daySectionFacts.setTextColor(color);
                daySectionDescription.setTextColor(color);
                daySectionFunFact.setTextColor(color);
                daySectionWebsite.setTextColor(color);
                toolbarBox.setCardBackgroundColor(color);
            }


        } else {
            if (new ColorExtraUtils().isColorLight(color)) {
                toolbar_title.setTextColor(ContextCompat.getColor(this, R.color.black));
                toolbar_date.setTextColor(ContextCompat.getColor(this, R.color.black));

                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dayinfo_close_black);

            } else {
                toolbar_title.setTextColor(ContextCompat.getColor(this, R.color.white));
                toolbar_date.setTextColor(ContextCompat.getColor(this, R.color.white));

                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dayinfo_close_white);

                collapsingToolbar.setExpandedTitleColor(0xffffffff);
                collapsingToolbar.setCollapsedTitleTextColor(0xffffffff);
            }

            collapsingToolbar.setScrimsShown(true);
            collapsingToolbar.setContentScrimColor(color);
            daySectionFacts.setTextColor(color);
            daySectionDescription.setTextColor(color);
            daySectionFunFact.setTextColor(color);
            daySectionWebsite.setTextColor(color);
            toolbarBox.setCardBackgroundColor(color);

            // Lollipop and up
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(title, icon, color);
                setTaskDescription(td);
                icon.recycle();
            }

        }
    }

    public void feedback(MenuItem item) {
        FeedbackFragment feedbackFragment = new FeedbackFragment();
        feedbackFragment.passData("DayInfo." + id + "." + title);

        BottomSheetDialogFragment bottomSheetDialogFragment = feedbackFragment;
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

    }

    public void share(MenuItem item) {

        final Context context = this;

        class shareAsync extends AsyncTask<String, Void, Uri> {

            private ProgressDialog dialog = new ProgressDialog(context);

            protected void onPreExecute() {

                this.dialog.setCancelable(true);
                this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    public void onCancel(DialogInterface dialog) {
                        shareAsync.this.cancel(true);
                        snackbar(getString(R.string.share_snackbar_dismissed));
                    }
                });

                this.dialog.setMessage(getString(R.string.share_dialog_description));
                this.dialog.setTitle(getString(R.string.share_dialog_title));
                this.dialog.show();
            }

            @Override
            protected Uri doInBackground(String... strings) {


                    if (!hasLoadedFullSize) {
                        try {
                            URL url = new URL(IMAGE_DAYINFO + id);
                            con = (HttpURLConnection) url.openConnection();
                            con.setDoInput(true);
                            con.connect();
                            input = con.getInputStream();
                        } catch (IOException e) {
                            FirebaseCrash.report(e);
                        }

                        bitmap = BitmapFactory.decodeStream(input);
                    }


                    Uri contentUri = null;

                    // save bitmap to cache directory
                    try {
                        File cachePath = new File(DayInfo.this.getCacheDir(), "images");
                        cachePath.mkdirs();
                        FileOutputStream stream = new FileOutputStream(cachePath + "/image.png");
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        stream.close();
                        File imagePath = new File(DayInfo.this.getCacheDir(), "images");
                        File newFile = new File(imagePath, "image.png");
                        contentUri = FileProvider.getUriForFile(DayInfo.this, "com.alvarlagerlof.temadagarapp.fileprovider", newFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        FirebaseCrash.report(e);
                    }

                    context.revokeUriPermission(contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    return contentUri;


            }

            protected void onPostExecute(Uri contentUri) {
                if (contentUri != null) {

                    String shareText;
                    String dateCurrent = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

                    if (dateCurrent.equals(date)) {
                        shareText = "Idag är det " + title + "! Fler dagar finns på appen Temadagar: https://play.google.com/store/apps/details?id=com.alvarlagerlof.temadagarapp";
                    } else {
                        shareText = "Ta en titt på " + title + " på appen Temadagar: https://play.google.com/store/apps/details?id=com.alvarlagerlof.temadagarapp";
                    }

                    shareIntent = new Intent(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_STREAM, contentUri)
                            .putExtra(Intent.EXTRA_TEXT, shareText)
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
                            .setType("image/png")
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(Intent.createChooser(shareIntent, "Dela via..."));

                }

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }


            }
        }

        shareAsync shareAsync = new shareAsync();
        shareAsync.execute("hoho");

    }


    public static void snackbar(String title) {

        final Snackbar snackbar = Snackbar.make(coordinatorLayout, title, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(16f);

        snackbar.show();

    }

    public void website(View view) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.black));

        builder.setStartAnimations(DayInfo.this, R.anim.slide_in, R.anim.stay);
        builder.setExitAnimations(DayInfo.this, R.anim.stay, R.anim.slide_out);
        builder.setShowTitle(true);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(DayInfo.this, Uri.parse(website));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                overridePendingTransition(R.anim.stay, R.anim.slide_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dayinfo, menu);

        if (new ColorExtraUtils().isColorLight(color)) {
            if (toolbar.getMenu() != null) {
                toolbar.getMenu().findItem(R.id.share_white).setVisible(false);
                toolbar.getMenu().findItem(R.id.feedback_white).setVisible(false);

                toolbar.getMenu().findItem(R.id.share_black).setVisible(true);
                toolbar.getMenu().findItem(R.id.feedback_black).setVisible(true);
            }
        } else {
            if (toolbar.getMenu() != null) {
                toolbar.getMenu().findItem(R.id.share_black).setVisible(false);
                toolbar.getMenu().findItem(R.id.feedback_black).setVisible(false);

                toolbar.getMenu().findItem(R.id.share_white).setVisible(true);
                toolbar.getMenu().findItem(R.id.feedback_white).setVisible(true);
            }
        }


        return true;
    }

    @Override
    public void onBackPressed() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription("Temadagar", icon, ContextCompat.getColor(this, R.color.colorPrimary));
            setTaskDescription(td);
            icon.recycle();
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_out);

    }*/



}
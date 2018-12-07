package com.alvarlagerlof.temadagarapp.Search;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alvarlagerlof.temadagarapp.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;


/**
 * Created by alvar on 2016-06-25.
 */
public class Search extends AppCompatActivity {

    RecyclerView mRecyclerView;
    Toolbar toolbar;
    public static EditText searchEdittext;
    LinearLayout clearButton;

    public static CoordinatorLayout coordinatorLayout;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        StrictMode.enableDefaults();



        toolbar           = (Toolbar) findViewById(R.id.toolbar);
        searchEdittext    = (EditText) findViewById(R.id.search_edittext);
        clearButton       = (LinearLayout) findViewById(R.id.clear);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatior_layout);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        clearButton.setVisibility(View.GONE);

        searchEdittext.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(searchEdittext, 0);
            }
        }, 50);

        getWindow().setBackgroundDrawable(null);

        searchEdittext.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable q) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //getResults getResults = new getResults();
               // getResults.execute(String.valueOf(s));

                if ((String.valueOf(s).equals(""))) {
                    clearButton.setVisibility(View.GONE);
                } else {
                    clearButton.setVisibility(View.VISIBLE);
                    Bundle params = new Bundle();
                    params.putString(FirebaseAnalytics.Param.SEARCH_TERM, String.valueOf(s));
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, params);
                }

            }
        });

        ArrayList<SearchObject> list = new ArrayList<>();
        SearchAdapter searchAdapter = new SearchAdapter(list, getSupportFragmentManager(), Search.this);
        list.add(new SearchObject("0", "foo", "foo","foo", "", true, "", "", "", "",  1));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setScrollContainer(false);
        mRecyclerView.setAdapter(searchAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                InputMethodManager imm = (InputMethodManager) Search.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = Search.this.getCurrentFocus();
                if (view == null) {
                    view = new View(Search.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        //getResults getResults = new getResults();
        //getResults.execute(String.valueOf(""));

    }


    /*class getResults extends AsyncTask<String, Void, ArrayList<SearchObject>> {

        @Override
        protected ArrayList doInBackground(String... q) {
            String data = null;
            ArrayList<SearchObject> list = new ArrayList<>();

            if (q[0].equals("")) {
                data = "{'result':[{'id':'351', 'title':'0', 'date':'foo', 'description':'foo', 'introduced':'foo', 'international':true, 'website':'foo', 'fun_fact':'foo', 'popularity':'foo', 'color':'foo', 'type':1}]}";
            } else {
                if (ConnectionUtils.isConnected(Search.this)) {
                    try {

                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(DATA_SEARCH + q[0])
                                .build();

                        Response response = client.newCall(request).execute();
                        data = response.body().string();
                        response.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                        FirebaseCrash.report(e);
                    }
                }
            }



            if (data != null) {
                try {
                    JSONObject json = new JSONObject(data);
                    JSONArray days = json.getJSONArray("result");


                    for (int i = 0; i < days.length(); i++) {
                        JSONObject day = days.getJSONObject(i);


                        String id = day.getString("id");
                        String title = day.getString("title");
                        String date = null;
                        if (day.getString("date").contains("-")) {
                            date = new DateStringConvertor().convert(day.getString("date"), false);
                        } else {
                            date = day.getString("date");
                        }
                        String color = day.getString("color");
                        String description = day.getString("description");
                        String introduced = day.getString("introduced");
                        Boolean international = day.getBoolean("international");
                        String website = day.getString("website");
                        String fun_fact = day.getString("fun_fact");
                        String popularity = day.getString("popularity");
                        int type = day.getInt("type");

                        SearchObject object = new SearchObject(id, title, date, description, introduced, international, website, fun_fact, popularity, color, type);

                        list.add(i, object);

                    }

                } catch (JSONException e) {
                    FirebaseCrash.report(e);
                    Log.e("error", e.toString());
                }

                return list;
            }

            return null;

        }

        protected void onPostExecute(ArrayList<SearchObject> list) {

            if (list != null) {

                SearchAdapter searchAdapter = new SearchAdapter(list, getSupportFragmentManager(), Search.this);
                mRecyclerView.swapAdapter(searchAdapter, true);
            }

        }
    }*/

    public void clear(View view) {
        searchEdittext.setText("");
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(searchEdittext, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void add() {
        //AddFragment addFragment = new AddFragment();
        //addFragment.passData(String.valueOf(searchEdittext.getText()));
    }

    public static void snackbar(String title) {

        final Snackbar snackbar = Snackbar.make(coordinatorLayout, title, Snackbar.LENGTH_LONG);

        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(16f);

        snackbar.show();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                InputMethodManager imm = (InputMethodManager) Search.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = Search.this.getCurrentFocus();
                if (view == null) {
                    view = new View(Search.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                supportFinishAfterTransition();
                overridePendingTransition(R.anim.stay, R.anim.slide_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_out);
    }

}

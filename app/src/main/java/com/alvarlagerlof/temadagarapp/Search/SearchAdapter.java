package com.alvarlagerlof.temadagarapp.Search;

/**
 * Created by alvar on 2015-07-10.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alvarlagerlof.temadagarapp.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SearchObject> dataset;

    private static final int TYPE_HEADER      = 0;
    private static final int TYPE_EMPTY_STATE = 1;
    private static final int TYPE_ITEM        = 2;

    private static final String TYPE_NO_QUERY   = "0";
    private static final String TYPE_NO_RESULTS = "1";

    Context context;

    FragmentManager fragmentManager;


    public SearchAdapter(ArrayList<SearchObject> dataset, FragmentManager fragmentManager, Context context) {
        this.dataset = dataset;
        this.fragmentManager = fragmentManager;
        this.context = context;
    }


    public static class ViewHolderHeader extends RecyclerView.ViewHolder  {
        public final TextView text1;

        public ViewHolderHeader(View itemView){
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.text1);
        }
    }

    public static class ViewHolderEmptyState extends RecyclerView.ViewHolder {
        public final ImageView image;
        public final TextView text1;
        public final TextView text2;
        public final AppCompatButton button;


        public ViewHolderEmptyState(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text1 = (TextView) itemView.findViewById(R.id.text1);
            text2 = (TextView) itemView.findViewById(R.id.text2);
            button = (AppCompatButton) itemView.findViewById(R.id.button);
        }

    }

    public static class ViewHolderResult extends RecyclerView.ViewHolder {
        public final TextView text1;

        public ViewHolderResult(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.text1);
        }

    }







    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View headerView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_header, viewGroup, false);
        View emptyStateView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_empty_state, viewGroup, false);
        View resultView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item, viewGroup, false);

        switch (i) {
            case TYPE_HEADER:
                return new ViewHolderHeader(headerView);
            case TYPE_EMPTY_STATE:
                return new ViewHolderEmptyState(emptyStateView);
            case TYPE_ITEM:
                return new ViewHolderResult(resultView);
        }

        throw new RuntimeException("there is no type that matches the type " + i + " + make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolderHeader) {

            ((ViewHolderHeader) holder).text1.setText(dataset.get(position).title);
            ((ViewHolderHeader) holder).text1.setTypeface(null, Typeface.BOLD);

        } else if (holder instanceof ViewHolderEmptyState) {

            if (dataset.get(position).title.equals(TYPE_NO_QUERY)) {

                ((ViewHolderEmptyState) holder).button.setVisibility(View.GONE);
                ((ViewHolderEmptyState) holder).image.setVisibility(View.GONE);

                int padding_in_dp = 30;
                final float scale = context.getResources().getDisplayMetrics().density;
                int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
                ((ViewHolderEmptyState) holder).text1.setPadding(0, padding_in_px, 0, 0);
                ((ViewHolderEmptyState) holder).text1.setTextColor(ContextCompat.getColor(context, R.color.textColorSecondary));


                ((ViewHolderEmptyState) holder).text1.setText("Sök efter en dag");
                ((ViewHolderEmptyState) holder).text2.setText("T.ex. \"kanelbullens dag\"");

            } else if (dataset.get(position).title.equals(TYPE_NO_RESULTS)) {

                Glide.with(context)
                        .load(R.raw.sad)
                        .into(((ViewHolderEmptyState) holder).image);

                ((ViewHolderEmptyState) holder).button.setVisibility(View.VISIBLE);
                ((ViewHolderEmptyState) holder).image.setVisibility(View.VISIBLE);

                ((ViewHolderEmptyState) holder).text1.setPadding(0, 0, 0, 0);
                ((ViewHolderEmptyState) holder).text1.setTextColor(ContextCompat.getColor(context, R.color.textColor));

                ((ViewHolderEmptyState) holder).text1.setText("Inga träffar");
                ((ViewHolderEmptyState) holder).text2.setText("Kontrollera stavningen eller föreslå en ny dag");


                ((ViewHolderEmptyState) holder).button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(((ViewHolderEmptyState) holder).button.getWindowToken(), 0);

                        //AddFragment addFragment = new AddFragment();
                        //addFragment.passData(dataset.get(position).date);

                        //BottomSheetDialogFragment bottomSheetDialogFragment = addFragment;
                        //bottomSheetDialogFragment.show(fragmentManager, bottomSheetDialogFragment.getTag());
                    }
                });

            }

        } else {

            ((ViewHolderResult) holder).text1.setText(dataset.get(position).title);

            /*((ViewHolderResult) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AsyncTask<Void, Void, Bitmap>() {
                        @Override
                        protected Bitmap doInBackground(Void... params) {
                            Bitmap theBitmap = null;
                            try {
                                theBitmap = Glide.
                                        with(context).
                                        load("https://api.temadagar.ravla.org/" + API_VERSION + "/get_image.php?w=300&h=225&id=" + dataset.get(position).id).
                                        diskCacheStrategy(DiskCacheStrategy.ALL).
                                        into(-1,-1).
                                        get();
                            } catch (final ExecutionException e) {
                                FirebaseCrash.report(e);
                            } catch (final InterruptedException e) {
                                FirebaseCrash.report(e);
                            }
                            return theBitmap;
                        }
                        @Override
                        protected void onPostExecute(Bitmap bitmap) {
                            if (null != bitmap) {
                                Intent intent = new Intent(context, DayInfo.class);
                                intent.putExtra("id", dataset.get(position).id);
                                intent.putExtra("title", dataset.get(position).title);
                                intent.putExtra("date", dataset.get(position).date);
                                intent.putExtra("description", dataset.get(position).description);
                                intent.putExtra("introduced", dataset.get(position).introduced);
                                intent.putExtra("international", dataset.get(position).international);
                                intent.putExtra("website", dataset.get(position).website);
                                intent.putExtra("fun_fact", dataset.get(position).fun_fact);
                                intent.putExtra("popularity", dataset.get(position).popularity);
                                intent.putExtra("color", dataset.get(position).color);
                                intent.putExtra("bitmap", bitmap);

                                Bundle bundle = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in, R.anim.stay).toBundle();
                                context.startActivity(intent, bundle);
                            };
                        }
                    }.execute();
                }
            });*/
        }
    }


    @Override
    public int getItemViewType(int position) {

        int type = dataset.get(position).type;

        if (position == 0 && type != TYPE_EMPTY_STATE) {
            return TYPE_HEADER;
        }

        switch (type) {
            case TYPE_EMPTY_STATE:
                return TYPE_EMPTY_STATE;
            case TYPE_ITEM:
                return TYPE_ITEM;
            default:
                return TYPE_HEADER;
        }

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


}
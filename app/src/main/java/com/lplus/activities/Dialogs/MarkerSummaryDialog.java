package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.DBHelper.AddFavoutiteTable;
import com.lplus.activities.DBHelper.AddRateTable;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.GetReviewsInterface;
import com.lplus.activities.Interfaces.ReviewsStatusInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.FavouriteObject;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Server.GetReviewsServerClass;
import com.lplus.activities.Server.ReviewsStatusServerClass;
import com.lplus.activities.activities.MarkerDescriptionActivity;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 22-03-2018.
 */

public class MarkerSummaryDialog implements View.OnClickListener, ReviewsStatusInterface, GetReviewsInterface {

    private Context context;
    private Dialog markerSummaryDialog;
    private MarkerObject markerObject;
    private TextView place_name, place_category, place_facilities, fav_tv, rate_total, user_count;
    private LinearLayout direction_layout, desc_layout, fav_layout, rate_layout;
    private AddFavoutiteTable addFavoutiteTable;
    private ImageView fav_iv, star1, star2, star3, star4,star5;
    private  TinyDB tinyDB;
    private AddRateTable addRateTable;
    private Bitmap snap;

    public MarkerSummaryDialog(Context context, MarkerObject markerObject, Bitmap snap)
    {
        this.context = context;
        this.markerObject = markerObject;
        addFavoutiteTable = new AddFavoutiteTable(context);
        addRateTable = new AddRateTable(context);
        this.snap = snap;
        Init();
    }

    private void Init()
    {

        markerSummaryDialog = new Dialog(context, R.style.CustomDialogTheme);
        markerSummaryDialog.setContentView(R.layout.dialog_marker_summary);
        markerSummaryDialog.setCancelable(true);
        markerSummaryDialog.setCanceledOnTouchOutside(true);
        markerSummaryDialog.getWindow().getAttributes().windowAnimations = R.style.SlideHorizontalAnimation;

        fav_iv = markerSummaryDialog.findViewById(R.id.favourite_iv);
        fav_tv = markerSummaryDialog.findViewById(R.id.favourite_tv);
        rate_total = markerSummaryDialog.findViewById(R.id.rate_total);
        user_count = markerSummaryDialog.findViewById(R.id.user_count);
        rate_total.setText("0.0");
        user_count.setText("(0)");

        //fetch all ID's from View
        place_name = markerSummaryDialog.findViewById(R.id.place_name);
        place_category = markerSummaryDialog.findViewById(R.id.place_category);
        place_facilities = markerSummaryDialog.findViewById(R.id.facility_available);

        direction_layout = markerSummaryDialog.findViewById(R.id.direction_layout);
        desc_layout = markerSummaryDialog.findViewById(R.id.desc_layout);
        fav_layout = markerSummaryDialog.findViewById(R.id.fav_layout);
        rate_layout = markerSummaryDialog.findViewById(R.id.rate_layout);

        star1 = markerSummaryDialog.findViewById(R.id.summary_star1);
        star2 = markerSummaryDialog.findViewById(R.id.summary_star2);
        star3 = markerSummaryDialog.findViewById(R.id.summary_star3);
        star4 = markerSummaryDialog.findViewById(R.id.summary_star4);
        star5 = markerSummaryDialog.findViewById(R.id.summary_star5);

        checkforRate();
        checkforFavorites();
        setData();
        //set listeners for linear layouts
        direction_layout.setOnClickListener(this);
        desc_layout.setOnClickListener(this);
        fav_layout.setOnClickListener(this);
        rate_layout.setOnClickListener(this);
    }

    private void setData()
    {
        //place data to views in dialog
        place_name.setText(markerObject.getMarkerName());
        place_category.setText(markerObject.getMarkerCategory());
        ArrayList<String> plac_fac = markerObject.getMarkerFacilities();

        place_facilities.setText(markerObject.getMarkerFacilities().toString().replace("[", "").replace("]", ""));
    }

    public void ShowDialog()
    {
        markerSummaryDialog.show();
    }

    public void HideDialog()
    {
        markerSummaryDialog.cancel();
    }

    private void checkforFavorites()
    {
        if(addFavoutiteTable ==null)
        {
            addFavoutiteTable = new AddFavoutiteTable(context);
        }
        boolean isFavorite = addFavoutiteTable.isFavourite(markerObject.getMarkerID());
        if(isFavorite)
        {
            fav_iv.setImageResource(R.drawable.icons8_heart_red_96);
            fav_tv.setText("Added");
        }
    }

    public void checkforRate() {
        if(addRateTable == null)
        {
            addRateTable = new AddRateTable(context);
        }
        boolean isRateAvailable = addRateTable.isRateAvailable(markerObject.getMarkerID());
        if(isRateAvailable) {
            Double rate = addRateTable.GetRateById(markerObject.getMarkerID());
            int users = addRateTable.GetUsersById(markerObject.getMarkerID());
            user_count.setText("("+users+")");
            rate_total.setText(String.valueOf(rate));
            if(rate == 0.0) {
                clearAllStar();
            }else if(rate == 1.0) {
                selectOneStar();
            }else if(rate == 2.0) {
                selectTwoStar();
            }else if(rate == 3.0) {
                selectThreeStar();
            }else if(rate == 4.0) {
                selectFourStar();
            }else if(rate == 5.0) {
                selectFiveStar();
            }else if(rate > 0.0 && rate < 1.0) {
                clearAllStar();
                star1.setImageResource(R.drawable.icons8_star_half_empty_96);
            } else if(rate > 1.0 && rate < 2.0) {
                selectOneStar();
                star2.setImageResource(R.drawable.icons8_star_half_empty_96);
            }else if(rate > 2.0 && rate < 3.0) {
                selectTwoStar();
                star3.setImageResource(R.drawable.icons8_star_half_empty_96);
            }else if(rate > 3.0 && rate < 4.0) {
                selectThreeStar();
                star4.setImageResource(R.drawable.icons8_star_half_empty_96);
            }else if(rate > 4.0 && rate < 5.0) {
                selectFourStar();
                star5.setImageResource(R.drawable.icons8_star_half_empty_96);
            }
        }else {
            clearAllStar();
        }
        addRateTable.CloseConnection();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.direction_layout:
            {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+markerObject.getMarkerLatitude()+","+markerObject.getMarkerLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
                break;
            }

            case R.id.desc_layout:
            {
                markerSummaryDialog.dismiss();
                //Check if Review update required

                ReviewsStatusServerClass reviewsStatusServerClass = new ReviewsStatusServerClass(context, markerObject);
                reviewsStatusServerClass.SetListener(this);
                reviewsStatusServerClass.execute();
                break;
            }

            case R.id.fav_layout:
            {
                if(addFavoutiteTable ==null)
                {
                    addFavoutiteTable = new AddFavoutiteTable(context);
                }
                if(addFavoutiteTable.isFavourite(markerObject.getMarkerID())) {
                    addFavoutiteTable.RemoveFavourite(markerObject.getMarkerID());
                    fav_iv.setImageResource(R.drawable.icons8_heart_white_96);
                    fav_tv.setText("Add To Favourites");
                    Toast.makeText(context,"Removed From Favourites",Toast.LENGTH_SHORT).show();
                } else {
                    FavouriteObject favouriteObject = new FavouriteObject(markerObject.getMarkerID(),markerObject.getMarkerName(),markerObject.getMarkerAddress(),markerObject.getMarkerLatitude(),markerObject.getMarkerLongitude());
                    addFavoutiteTable.SaveRecord(favouriteObject);
                    fav_iv.setImageResource(R.drawable.icons8_heart_red_96);
                    fav_tv.setText("Added");
                    Toast.makeText(context,"Added To Favourites",Toast.LENGTH_SHORT).show();
                }
                addFavoutiteTable.CloseConnection();
                break;
            }

            case R.id.rate_layout:
            {
                break;
            }
        }
    }

    @Override
    public void onUpdateRequired(int reviewResponse, int photoResponse)
    {
        tinyDB = TinyDB.Init(context);
        int savedReviewStatus = tinyDB.getInt(markerObject.getMarkerID());
        if(savedReviewStatus < reviewResponse)
        {
            //Request for Reviews
            tinyDB.putInt(markerObject.getMarkerID(), reviewResponse);
            GetReviewsServerClass getReviewsServerClass = new GetReviewsServerClass(context, markerObject);
            getReviewsServerClass.SetListener(this);
            getReviewsServerClass.execute();
        }
        else
        {
            System.out.println("Reviews fetched: ");
            Intent intent = new Intent(context, MarkerDescriptionActivity.class);
            tinyDB = TinyDB.Init(context);
            String imagePath = tinyDB.putImage("snap","image", snap);
            tinyDB.putString("snap", imagePath);
            tinyDB.putObject(Keys.MARKER_OBJECT, markerObject);
            context.startActivity(intent);
        }
    }

    @Override
    public void onUpdateNotRequired()
    {
        Toast.makeText(context, "Review Update Failed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, MarkerDescriptionActivity.class);
        tinyDB = TinyDB.Init(context);
        String imagePath = tinyDB.putImage("snap","image", snap);
        tinyDB.putString("snap", imagePath);
        tinyDB.putObject(Keys.MARKER_OBJECT, markerObject);
        context.startActivity(intent);
    }

    public void selectOneStar()
    {
        star1.setImageResource(R.drawable.icons8_star_filled_96);
        star2.setImageResource(R.drawable.icons8_star_96);
        star3.setImageResource(R.drawable.icons8_star_96);
        star4.setImageResource(R.drawable.icons8_star_96);
        star5.setImageResource(R.drawable.icons8_star_96);
    }
    public void selectTwoStar()
    {
        star1.setImageResource(R.drawable.icons8_star_filled_96);
        star2.setImageResource(R.drawable.icons8_star_filled_96);
        star3.setImageResource(R.drawable.icons8_star_96);
        star4.setImageResource(R.drawable.icons8_star_96);
        star5.setImageResource(R.drawable.icons8_star_96);
    }
    public void selectThreeStar()
    {
        star1.setImageResource(R.drawable.icons8_star_filled_96);
        star2.setImageResource(R.drawable.icons8_star_filled_96);
        star3.setImageResource(R.drawable.icons8_star_filled_96);
        star4.setImageResource(R.drawable.icons8_star_96);
        star5.setImageResource(R.drawable.icons8_star_96);
    }
    public void selectFourStar()
    {
        star1.setImageResource(R.drawable.icons8_star_filled_96);
        star2.setImageResource(R.drawable.icons8_star_filled_96);
        star3.setImageResource(R.drawable.icons8_star_filled_96);
        star4.setImageResource(R.drawable.icons8_star_filled_96);
        star5.setImageResource(R.drawable.icons8_star_96);
    }
    public void selectFiveStar()
    {
        star1.setImageResource(R.drawable.icons8_star_filled_96);
        star2.setImageResource(R.drawable.icons8_star_filled_96);
        star3.setImageResource(R.drawable.icons8_star_filled_96);
        star4.setImageResource(R.drawable.icons8_star_filled_96);
        star5.setImageResource(R.drawable.icons8_star_filled_96);
    }
    public void clearAllStar()
    {
        star1.setImageResource(R.drawable.icons8_star_96);
        star2.setImageResource(R.drawable.icons8_star_96);
        star3.setImageResource(R.drawable.icons8_star_96);
        star4.setImageResource(R.drawable.icons8_star_96);
        star5.setImageResource(R.drawable.icons8_star_96);
    }

    @Override
    public void onReviewFetchStatus(boolean status) {
        if (status)
        {
            System.out.println("Reviews fetched: ");
            Intent intent = new Intent(context, MarkerDescriptionActivity.class);
            tinyDB = TinyDB.Init(context);
            String imagePath = tinyDB.putImage("snap","image", snap);
            tinyDB.putString("snap", imagePath);
            tinyDB.putObject(Keys.MARKER_OBJECT, markerObject);
            context.startActivity(intent);
        }
        else
        {
            Toast.makeText(context, "Review Update Failed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, MarkerDescriptionActivity.class);
            tinyDB = TinyDB.Init(context);
            String imagePath = tinyDB.putImage("snap","image", snap);
            tinyDB.putString("snap", imagePath);
            tinyDB.putObject(Keys.MARKER_OBJECT, markerObject);
            context.startActivity(intent);
        }
    }
}

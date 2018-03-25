package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.DBHelper.AddFavoutiteTable;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.GetReviewsInterface;
import com.lplus.activities.Interfaces.ReviewsStatusInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.FavouriteObject;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Server.GetReviewsServerClass;
import com.lplus.activities.Server.ReviewsStatusServerClass;
import com.lplus.activities.activities.MarkerDescriptionActivity;

/**
 * Created by Sai_Kameswari on 22-03-2018.
 */

public class MarkerSummaryDialog implements View.OnClickListener, ReviewsStatusInterface, GetReviewsInterface {

    private Context context;
    private Dialog markerSummaryDialog;
    private MarkerObject markerObject;
    private TextView place_name, place_category, place_facilities, fav_tv;
    private LinearLayout direction_layout, desc_layout, fav_layout, rate_layout;
    private AddFavoutiteTable addFavoutiteTable;
    private ImageView fav_iv;
    private  TinyDB tinyDB;

    public MarkerSummaryDialog(Context context, MarkerObject markerObject)
    {
        this.context = context;
        this.markerObject = markerObject;
        addFavoutiteTable = new AddFavoutiteTable(context);
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

        //fetch all ID's from View
        place_name = markerSummaryDialog.findViewById(R.id.place_name);
        place_category = markerSummaryDialog.findViewById(R.id.place_category);
        place_facilities = markerSummaryDialog.findViewById(R.id.facility_available);

        direction_layout = markerSummaryDialog.findViewById(R.id.direction_layout);
        desc_layout = markerSummaryDialog.findViewById(R.id.desc_layout);
        fav_layout = markerSummaryDialog.findViewById(R.id.fav_layout);
        rate_layout = markerSummaryDialog.findViewById(R.id.rate_layout);

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
            tinyDB = new TinyDB(context);
            tinyDB.putObject(Keys.MARKER_OBJECT, markerObject);
            context.startActivity(intent);
        }
    }

    @Override
    public void onUpdateNotRequired()
    {
        Toast.makeText(context, "Review Update Failed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, MarkerDescriptionActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onReviewFetched()
    {
        System.out.println("Reviews fetched: ");
        Intent intent = new Intent(context, MarkerDescriptionActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onReviewNotFetched() {

        Toast.makeText(context, "Review Update Failed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, MarkerDescriptionActivity.class);
        context.startActivity(intent);
    }
}

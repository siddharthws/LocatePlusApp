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
import com.lplus.activities.Objects.FavouriteObject;
import com.lplus.activities.Objects.MarkerObject;

/**
 * Created by Sai_Kameswari on 22-03-2018.
 */

public class MarkerSummaryDialog implements View.OnClickListener {

    private Context context;
    private Dialog markerSummaryDialog;
    private MarkerObject markerObject;
    private TextView place_name, place_category, place_facilities;
    private LinearLayout direction_layout, desc_layout, fav_layout, rate_layout;
    private AddFavoutiteTable addFavoutiteTable;

    public MarkerSummaryDialog(Context context, MarkerObject markerObject)
    {
        this.context = context;
        this.markerObject = markerObject;
        Init();
    }

    private void Init()
    {
        markerSummaryDialog = new Dialog(context, R.style.CustomDialogTheme);
        markerSummaryDialog.setContentView(R.layout.dialog_marker_summary);
        markerSummaryDialog.setCancelable(true);
        markerSummaryDialog.setCanceledOnTouchOutside(true);

        //fetch all ID's from View
        place_name = markerSummaryDialog.findViewById(R.id.place_name);
        place_category = markerSummaryDialog.findViewById(R.id.place_category);
        place_facilities = markerSummaryDialog.findViewById(R.id.facility_available);

        direction_layout = markerSummaryDialog.findViewById(R.id.direction_layout);
        desc_layout = markerSummaryDialog.findViewById(R.id.desc_layout);
        fav_layout = markerSummaryDialog.findViewById(R.id.fav_layout);
        rate_layout = markerSummaryDialog.findViewById(R.id.rate_layout);

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
                MarkerDescriptionDialog markerDescriptionDialog = new MarkerDescriptionDialog(context, markerObject);
                markerDescriptionDialog.ShowDialog();
                break;
            }

            case R.id.fav_layout:
            {
                ImageView fav_iv = view.findViewById(R.id.favourite_iv);
                TextView fav_tv = view.findViewById(R.id.favourite_tv);
                addFavoutiteTable = new AddFavoutiteTable(context);
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

                break;
            }

            case R.id.rate_layout:
            {

                break;
            }

        }
    }
}

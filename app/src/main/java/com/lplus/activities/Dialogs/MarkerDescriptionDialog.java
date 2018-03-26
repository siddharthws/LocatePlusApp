package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Adapters.ReviewSliderAdapter;
import com.lplus.activities.DBHelper.ReviewsTable;
import com.lplus.activities.Extras.CacheData;
import com.lplus.activities.Interfaces.MarkerReviewInterface;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Objects.ReviewsObject;
import com.lplus.activities.Server.MarkerReviewServerClass;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sai_Kameswari on 22-03-2018.
 */

public class MarkerDescriptionDialog implements View.OnClickListener, MarkerReviewInterface {

    private Context context;
    private Dialog markerdescriptionDialog;
    private MarkerObject markerObject;
    private TextView dec_place_name, dec_category, desc_address, dec_facilities, tv_review;
    private LinearLayout direction_layout, desc_layout;
    private ImageButton review_send, flag_photo, flag_name_address_category, flag_facility;
    private LoadingDialog loadingDialog;
    private ViewPager mPager;
    private static int currentPage = 0;
    ArrayList<String> photo_uuid;
    ArrayList<String> photo_path;

    public MarkerDescriptionDialog(Context context, MarkerObject markerObject)
    {
        this.context = context;
        this.markerObject = markerObject;
        Init();
    }

    private void Init()
    {

        //fetch all ID's from View
        dec_place_name = markerdescriptionDialog.findViewById(R.id.dec_place_name);
        dec_category = markerdescriptionDialog.findViewById(R.id.dec_category);
        desc_address = markerdescriptionDialog.findViewById(R.id.desc_address);
        dec_facilities = markerdescriptionDialog.findViewById(R.id.dec_facilities);
        tv_review = markerdescriptionDialog.findViewById(R.id.tv_review);
        review_send = markerdescriptionDialog.findViewById(R.id.review_send);
        flag_photo = markerdescriptionDialog.findViewById(R.id.flag_photo);
        flag_name_address_category = markerdescriptionDialog.findViewById(R.id.flag_name_address_category);
        flag_facility = markerdescriptionDialog.findViewById(R.id.flag_facility);



        direction_layout = markerdescriptionDialog.findViewById(R.id.direction_layout);
        desc_layout = markerdescriptionDialog.findViewById(R.id.desc_layout);

        setData();
        //set listeners for linear layouts
        direction_layout.setOnClickListener(this);
        desc_layout.setOnClickListener(this);
        review_send.setOnClickListener(this);
        flag_photo.setOnClickListener(this);
        flag_name_address_category.setOnClickListener(this);
        flag_facility.setOnClickListener(this);
    }

    private void setData()
    {
        //place data to views in dialog
        dec_place_name.setText(markerObject.getMarkerName());
        dec_category.setText(markerObject.getMarkerCategory());
        desc_address.setText(markerObject.getMarkerAddress());
        String facility_item = "";
        ArrayList<String> items = markerObject.getMarkerFacilities();
        for(int i=0; i<items.size();i++)
        {
            facility_item = facility_item + items.get(i) + "\n";
        }
        dec_facilities.setText(facility_item);

        //check if Reviews are there
        if(CacheData.cacheAllReviews == null)
        {
            CacheData.cacheAllReviews = new ArrayList<>();
            ReviewsTable reviewsTable = new ReviewsTable(context);
            CacheData.cacheAllReviews.addAll(reviewsTable.ReadRecords());
            reviewsTable.CloseConnection();
        }
        beginSlide();
    }

    public void ShowDialog()
    {
        markerdescriptionDialog.show();
    }

    public void HideDialog()
    {
        markerdescriptionDialog.cancel();
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
                //call Rate place dialog
                RatePlaceDialog ratePlaceDialog = new RatePlaceDialog(context, markerObject);
                ratePlaceDialog.ShowDialog();
                break;
            }

            case R.id.review_send:
            {
                String review = tv_review.getText().toString();
                if (review.length() == 0)
                {
                    Toast.makeText(context, "Write Something", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadingDialog = new LoadingDialog(context, "Sending Review...");
                loadingDialog.ShowDialog();
                MarkerReviewServerClass markerReviewServerClass = new MarkerReviewServerClass(context, markerObject, review);
                markerReviewServerClass.SetListener(this);
                markerReviewServerClass.execute();
                break;
            }

            case R.id.flag_photo:
            {
                //call Rate place dialog
                RatePhotoDialog ratePhotoDialog = new RatePhotoDialog(context, markerObject, photo_uuid, photo_path);
                break;
            }

            case R.id.flag_name_address_category:
            {
                break;
            }
            case R.id.flag_facility:
            {

                break;
            }
        }
    }

    @Override
    public void onReviewSent()
    {
        loadingDialog.HideDialog();
        Toast.makeText(context, "Review Submitted..", Toast.LENGTH_SHORT).show();
        tv_review.setText("");
    }

    @Override
    public void onReviewFailed()
    {
        loadingDialog.HideDialog();
        Toast.makeText(context, "Review Not Sent..", Toast.LENGTH_SHORT).show();
    }

    public void beginSlide() {

        mPager =  markerdescriptionDialog.findViewById(R.id.review_pager);
        final ArrayList<String> reviews = new ArrayList<>();
        for(ReviewsObject reviewsObject : CacheData.cacheAllReviews)
        {
            reviews.addAll(reviewsObject.getReviews());
        }
        System.out.println("Reviews data: "+reviews.toString());
        mPager.setAdapter(new ReviewSliderAdapter(context,reviews));

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == reviews.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 3000);
    }

}

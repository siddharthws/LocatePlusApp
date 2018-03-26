package com.lplus.activities.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.lplus.R;
import com.lplus.activities.Adapters.ReviewSliderAdapter;
import com.lplus.activities.DBHelper.ReviewsTable;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Dialogs.RateCategoryDialog;
import com.lplus.activities.Dialogs.RatePhotoDialog;
import com.lplus.activities.Dialogs.RatePlaceDialog;
import com.lplus.activities.Extras.CacheData;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.MarkerReviewInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Objects.ReviewsObject;
import com.lplus.activities.Server.MarkerReviewServerClass;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MarkerDescriptionActivity extends HomeActivity implements View.OnClickListener, MarkerReviewInterface {

    private MarkerObject markerObject;
    private TextView dec_place_name, dec_category, desc_address, dec_facilities, tv_review;
    private LinearLayout direction_layout, desc_layout;
    private ImageButton review_send, flag_photo, flag_name_address_category, flag_facility;
    private LoadingDialog loadingDialog;
    private ViewPager mPager;
    private static int currentPage = 0;
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_place_description);

        Toolbar mToolbar =  findViewById(R.id.toolbar);
        mToolbar.setTitle("Marker Description");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Init();
    }
    private void Init()
    {
        //fetch marker object
        tinyDB = TinyDB.Init(this);
        markerObject = tinyDB.getObject(Keys.MARKER_OBJECT, MarkerObject.class);

        //fetch all ID's from View
        dec_place_name = findViewById(R.id.dec_place_name);
        dec_category = findViewById(R.id.dec_category);
        desc_address = findViewById(R.id.desc_address);
        dec_facilities = findViewById(R.id.dec_facilities);
        tv_review = findViewById(R.id.tv_review);
        review_send = findViewById(R.id.review_send);
        flag_photo = findViewById(R.id.flag_photo);
        flag_name_address_category = findViewById(R.id.flag_name_address_category);
        flag_facility = findViewById(R.id.flag_facility);



        direction_layout = findViewById(R.id.direction_layout);
        desc_layout = findViewById(R.id.desc_layout);

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
            ReviewsTable reviewsTable = new ReviewsTable(this);
            CacheData.cacheAllReviews.addAll(reviewsTable.ReadRecords());
            reviewsTable.CloseConnection();
        }
        beginSlide();
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
                startActivity(mapIntent);
                break;
            }

            case R.id.desc_layout:
            {
                RatePlaceDialog ratePlaceDialog = new RatePlaceDialog(this, markerObject);
                ratePlaceDialog.ShowDialog();
                break;
            }

            case R.id.review_send:
            {
                String review = tv_review.getText().toString();
                if (review.length() == 0)
                {
                    Toast.makeText(this, "Write Something", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadingDialog = new LoadingDialog(this, "Sending Review...");
                loadingDialog.ShowDialog();
                MarkerReviewServerClass markerReviewServerClass = new MarkerReviewServerClass(this, markerObject, review);
                markerReviewServerClass.SetListener(this);
                markerReviewServerClass.execute();
                break;
            }

            case R.id.flag_photo:
            {
                //call Rate place dialog
                RatePhotoDialog ratePhotoDialog = new RatePhotoDialog(this, markerObject);
                ratePhotoDialog.ShowDialog();
                break;
            }

            case R.id.flag_name_address_category:
            {
                //call Rate place dialog
                final GoogleMap googleMap = getMap();
                if(googleMap != null)
                {
                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            // Make a snapshot when map's done loading
                            googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                                @Override
                                public void onSnapshotReady(Bitmap bitmap) {
                                    RateCategoryDialog rateCategoryDialog = new RateCategoryDialog(MarkerDescriptionActivity.this, markerObject, bitmap);
                                    rateCategoryDialog.ShowDialog();
                                }
                            });
                        }
                    });
                }
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
        Toast.makeText(this, "Review Submitted..", Toast.LENGTH_SHORT).show();
        tv_review.setText("");
    }

    @Override
    public void onReviewFailed()
    {
        loadingDialog.HideDialog();
        Toast.makeText(this, "Review Not Sent..", Toast.LENGTH_SHORT).show();
    }

    public void beginSlide() {

        mPager =  findViewById(R.id.review_pager);
        final ArrayList<String> reviews = new ArrayList<>();
        for(ReviewsObject reviewsObject : CacheData.cacheAllReviews)
        {
            reviews.addAll(reviewsObject.getReviews());
        }
        System.out.println("Reviews data: "+reviews.toString());
        mPager.setAdapter(new ReviewSliderAdapter(this,reviews));

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

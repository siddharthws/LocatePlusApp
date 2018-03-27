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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.lplus.R;
import com.lplus.activities.Adapters.ReviewSliderAdapter;
import com.lplus.activities.DBHelper.AddFavoutiteTable;
import com.lplus.activities.DBHelper.AddRateTable;
import com.lplus.activities.DBHelper.ReviewsTable;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Dialogs.RateCANDialog;
import com.lplus.activities.Dialogs.RateFacilityDialog;
import com.lplus.activities.Dialogs.RatePhotoDialog;
import com.lplus.activities.Dialogs.RatePlaceDialog;
import com.lplus.activities.Extras.CacheData;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.MarkerReviewInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.FavouriteObject;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Objects.ReviewsObject;
import com.lplus.activities.Server.MarkerReviewServerClass;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class MarkerDescriptionActivity extends HomeActivity implements View.OnClickListener, MarkerReviewInterface {

    private MarkerObject markerObject;
    private TextView dec_place_name, dec_category, desc_address, dec_facilities, tv_review, rate_total;
    private LinearLayout direction_layout, desc_layout, rate_place;
    private ImageButton review_send, flag_photo, flag_name_address_category, flag_facility;
    private LoadingDialog loadingDialog;
    private ViewPager mPager;
    private static int currentPage = 0;
    private TinyDB tinyDB;
    private ArrayList<String> photo_uuid_array;
    private ArrayList<String> photo_path_array;
    private ImageView fav_iv, star1, star2, star3, star4,star5;
    private AddFavoutiteTable addFavoutiteTable;
    AddRateTable addRateTable;

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
        photo_uuid_array = new ArrayList<>();
        photo_path_array = new ArrayList<>();
        markerObject = tinyDB.getObject(Keys.MARKER_OBJECT, MarkerObject.class);
        addFavoutiteTable = new AddFavoutiteTable(this);

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
        rate_place = findViewById(R.id.rate_place_layout);
        rate_total = findViewById(R.id.rate_total);
        rate_total.setText("0.0");

        star1 = findViewById(R.id.desc_star1);
        star2 = findViewById(R.id.desc_star2);
        star3 = findViewById(R.id.desc_star3);
        star4 = findViewById(R.id.desc_star4);
        star5 = findViewById(R.id.desc_star5);
        fav_iv = findViewById(R.id.dec_fav);



        direction_layout = findViewById(R.id.direction_layout);
        desc_layout = findViewById(R.id.rate_place_layout);
        addRateTable = new AddRateTable(this);
        showRate();
        checkforFavorites();
        setData();
        //set listeners for linear layouts
        direction_layout.setOnClickListener(this);
        desc_layout.setOnClickListener(this);
        review_send.setOnClickListener(this);
        flag_photo.setOnClickListener(this);
        flag_facility.setOnClickListener(this);
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

    private void checkforFavorites()
    {
        if(addFavoutiteTable ==null)
        {
            addFavoutiteTable = new AddFavoutiteTable(this);
        }
        boolean isFavorite = addFavoutiteTable.isFavourite(markerObject.getMarkerID());
        if(isFavorite)
        {
            fav_iv.setImageResource(R.drawable.icons8_heart_red_96);
        }
    }

    public void showRate() {
        if(addRateTable == null)
        {
            addRateTable = new AddRateTable(this);
        }
        boolean isRateAvailable = addRateTable.isRateAvailable(markerObject.getMarkerID());
        if(isRateAvailable) {
            Double rate = addRateTable.GetRateById(markerObject.getMarkerID());
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
            case R.id.dec_fav:
            {
                if(addFavoutiteTable ==null)
                {
                    addFavoutiteTable = new AddFavoutiteTable(this);
                }
                if(addFavoutiteTable.isFavourite(markerObject.getMarkerID())) {
                    addFavoutiteTable.RemoveFavourite(markerObject.getMarkerID());
                    fav_iv.setImageResource(R.drawable.icons8_heart_white_96);
                    Toasty.success(this,"Removed From Favourites",Toast.LENGTH_SHORT,true).show();
                } else {
                    FavouriteObject favouriteObject = new FavouriteObject(markerObject.getMarkerID(),markerObject.getMarkerName(),markerObject.getMarkerAddress(),markerObject.getMarkerLatitude(),markerObject.getMarkerLongitude());
                    addFavoutiteTable.SaveRecord(favouriteObject);
                    fav_iv.setImageResource(R.drawable.icons8_heart_red_96);
                    Toasty.success(this,"Added To Favourites",Toast.LENGTH_SHORT,true).show();
                }
                addFavoutiteTable.CloseConnection();
                break;
            }
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
                RatePhotoDialog ratePhotoDialog = new RatePhotoDialog(this, markerObject, photo_uuid_array, photo_path_array);
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
                                    RateCANDialog rateCategoryDialog = new RateCANDialog(MarkerDescriptionActivity.this, markerObject, bitmap);
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
                RateFacilityDialog rateFacilityDialog = new RateFacilityDialog(this, markerObject);
                break;
            }
            case R.id.rate_place_layout:
            {
                RatePlaceDialog ratePlaceDialog = new RatePlaceDialog(this, markerObject);
                ratePlaceDialog.ShowDialog();
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

}

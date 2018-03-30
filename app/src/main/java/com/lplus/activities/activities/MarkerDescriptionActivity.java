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

import com.lplus.R;
import com.lplus.activities.Adapters.FetchedImageSlider;
import com.lplus.activities.Adapters.ReviewSliderAdapter;
import com.lplus.activities.DBHelper.AddFacilityTable;
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
import com.lplus.activities.Interfaces.FacilityDialogClickInterface;
import com.lplus.activities.Interfaces.MarkerReviewInterface;
import com.lplus.activities.Interfaces.PhotosDialogClickInterface;
import com.lplus.activities.Interfaces.RateFacillityInterface;
import com.lplus.activities.Interfaces.RatePhotosInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.FavouriteObject;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Objects.ReviewsObject;
import com.lplus.activities.Server.MarkerReviewServerClass;
import com.lplus.activities.Server.RateFacilityServerClass;
import com.lplus.activities.Server.RatePhotoServerClass;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class MarkerDescriptionActivity extends HomeActivity implements  View.OnClickListener,
                                                                        MarkerReviewInterface,
                                                                        FacilityDialogClickInterface,
                                                                        PhotosDialogClickInterface,
                                                                        RateFacillityInterface,
                                                                        RatePhotosInterface {

    private MarkerObject markerObject;
    private TextView dec_place_name, dec_category, desc_address, dec_facilities, tv_review, rate_total, user_count;
    private LinearLayout direction_layout, desc_layout, rate_place, desc_layout_fav;
    private ImageButton review_send, flag_photo, flag_name_address_category, flag_facility;
    private LoadingDialog loadingDialog;
    private ViewPager mPager, imagePager;
    private static int currentPage = 0;
    private TinyDB tinyDB;
    private ArrayList<String> photo_uuid_array, fac_id, fac_rate, photo_rate, can_rate;
    private ArrayList<String> photo_path_array;
    private ImageView fav_iv, star1, star2, star3, star4,star5;
    private AddFavoutiteTable addFavoutiteTable;
    private  int fac_count, photo_count;
    RateFacilityDialog rateFacilityDialog;
    RatePhotoDialog ratePhotoDialog;
    AddFacilityTable addFacilityTable;
    AddRateTable addRateTable;
    private Bitmap snap;
    private ArrayList<String> paths;


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

        Intent intent = getIntent();
        paths = intent.getStringArrayListExtra("paths");
        if (paths == null) {
            paths = new ArrayList<>();
        }
        //fetch marker object
        tinyDB = TinyDB.Init(this);
        photo_uuid_array = new ArrayList<>();
        photo_path_array = new ArrayList<>();
        markerObject = tinyDB.getObject(Keys.MARKER_OBJECT, MarkerObject.class);
        String imagePath = tinyDB.getString("snap");
        snap = tinyDB.getImage(imagePath);
        System.out.println("Bitmap snap: "+snap.toString());
        addFavoutiteTable = new AddFavoutiteTable(this);
        addFacilityTable = new AddFacilityTable(this);

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
        desc_layout_fav = findViewById(R.id.dec_fav_layout);
        rate_place = findViewById(R.id.rate_place_layout);
        rate_total = findViewById(R.id.rate_total);
        user_count  = findViewById(R.id.user_count);
        rate_total.setText("0.0");
        user_count.setText("(0)");
        fac_id = new ArrayList<>();
        fac_rate = new ArrayList<>();
        photo_rate = new ArrayList<>();
        can_rate = new ArrayList<>();
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
        setData();
        checkforFavorites();
        putImage(paths);
    }

    private void setData()
    {
        //place data to views in dialog
        dec_place_name.setText(markerObject.getMarkerName());
        dec_category.setText(markerObject.getMarkerCategory());
        desc_address.setText(markerObject.getMarkerAddress());
        StringBuilder facility_item = new StringBuilder();
        ArrayList<String> items = markerObject.getMarkerFacilities();

        for(int i = 0; i<items.size()-1;i++)
        {
            facility_item.append(items.get(i));
            facility_item.append(", ");
        }
        facility_item.append(items.get(items.size()-1));
        dec_facilities.setText(facility_item.toString());

        //check if Reviews are there
        if(CacheData.cacheAllReviews == null)
        {
            CacheData.cacheAllReviews = new ArrayList<>();
            ReviewsTable reviewsTable = new ReviewsTable(this);
            CacheData.cacheAllReviews.addAll(reviewsTable.ReadRecords());
            reviewsTable.CloseConnection();
        }
        beginSlide();


        //set listeners for linear layouts
        direction_layout.setOnClickListener(this);
        desc_layout_fav.setOnClickListener(this);
        desc_layout.setOnClickListener(this);
        review_send.setOnClickListener(this);
        flag_photo.setOnClickListener(this);
        flag_facility.setOnClickListener(this);
        flag_name_address_category.setOnClickListener(this);
        flag_facility.setOnClickListener(this);
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
            int users = addRateTable.GetUsersById(markerObject.getMarkerID());
            rate_total.setText(String.valueOf(rate));
            user_count.setText("("+users+")");
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
        }
        else {
            clearAllStar();
        }
        addRateTable.CloseConnection();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.dec_fav_layout:
            {
                if(addFavoutiteTable ==null)
                {
                    addFavoutiteTable = new AddFavoutiteTable(this);
                }
                if(addFavoutiteTable.isFavourite(markerObject.getMarkerID())) {
                    addFavoutiteTable.RemoveFavourite(markerObject.getMarkerID());
                    fav_iv.setImageResource(R.drawable.icons8_heart_black_96);
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
                photo_count = 0;
                ratePhotoDialog = new RatePhotoDialog(MarkerDescriptionActivity.this, markerObject, photo_path_array, photo_uuid_array, photo_count);
                ratePhotoDialog.SetListener(this);
                ratePhotoDialog.ShowDialog();
                break;
            }

            case R.id.flag_name_address_category:
            {
                System.out.println("Reaching to the flag of address");
                RateCANDialog rateCategoryDialog = new RateCANDialog(MarkerDescriptionActivity.this, markerObject, snap);
                rateCategoryDialog.ShowDialog();
                break;
            }
            case R.id.flag_facility:
            {
                fac_count = 0;
                rateFacilityDialog = new RateFacilityDialog(MarkerDescriptionActivity.this, markerObject, fac_count);
                rateFacilityDialog.SetListener(this);
                rateFacilityDialog.ShowDialog();

                /**/
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

    public void beginSlide() {

        mPager =  findViewById(R.id.review_pager);
        final ArrayList<String> reviews = new ArrayList<>();
        for(ReviewsObject reviewsObject : CacheData.cacheAllReviews)
        {
            reviews.addAll(reviewsObject.getReviews());
        }
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

    public void putImage(ArrayList<String>  image) {
        imagePager =  findViewById(R.id.photo_pager);
        final ArrayList<String> images = new ArrayList<>();
        images.addAll(image);
        imagePager.setAdapter(new FetchedImageSlider(this,images));

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == images.size()) {
                    currentPage = 0;
                }
                imagePager.setCurrentItem(currentPage++, true);
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

    @Override
    public void onDialogClick(String givenRating)
    {
        fac_count++;
        fac_rate.add(givenRating);
        if(fac_count != markerObject.getMarkerFacilities().size())
        {
            System.out.println("fac_count position : "+fac_count);
            rateFacilityDialog = new RateFacilityDialog(MarkerDescriptionActivity.this, markerObject, fac_count);
            rateFacilityDialog.SetListener(this);
            rateFacilityDialog.ShowDialog();
        }
        else
        {
            for(String fac_value : markerObject.getMarkerFacilities()) {
                System.out.println("fac_value : "+fac_value);
                String id = addFacilityTable.ReadID(fac_value);
                System.out.println("fac_id : "+id);
                fac_id.add(id);
            }
            System.out.println("fac_count position : "+fac_id.toString());
            addFacilityTable.CloseConnection();
            loadingDialog = new LoadingDialog(MarkerDescriptionActivity.this, "Please Wait...");
            loadingDialog.ShowDialog();

            RateFacilityServerClass rateFacilityServerClass = new RateFacilityServerClass(this, markerObject, fac_id, fac_rate);
            rateFacilityServerClass.SetListener(this);
            rateFacilityServerClass.execute();

        }
    }

    @Override
    public void onPhotosDialogClick(String rating) {

        photo_count++;
        photo_rate.add(rating);
        if(photo_count != photo_path_array.size())
        {
            System.out.println("photo_count position : "+photo_count);
            ratePhotoDialog = new RatePhotoDialog(MarkerDescriptionActivity.this, markerObject,photo_path_array,photo_uuid_array, fac_count);
            ratePhotoDialog.SetListener(this);
            ratePhotoDialog.ShowDialog();
        }
        else
        {
            loadingDialog = new LoadingDialog(this, "Please Wait...");
            loadingDialog.ShowDialog();
            RatePhotoServerClass ratePhotoServerClass = new RatePhotoServerClass(this, markerObject, photo_uuid_array, photo_rate);
            ratePhotoServerClass.SetListener(this);
            ratePhotoServerClass.execute();
            photo_rate.clear();
        }
    }

    @Override
    public void onPhotoSent(boolean status) {
        if (status)
        {
            loadingDialog.HideDialog();
            Toasty.success(this,"Photos Successfully Rated", Toast.LENGTH_SHORT,true).show();
        }
        else
        {
            loadingDialog.HideDialog();
            Toasty.error(this,"Photos Rating Failed", Toast.LENGTH_SHORT,true).show();
        }
    }

    @Override
    public void onFacilityFetchStatus(boolean status) {
        if (status)
        {
            fac_rate.clear();
            loadingDialog.HideDialog();
            Toasty.success(this,"Facilities Successfully Rated", Toast.LENGTH_SHORT,true).show();
        }
        else
        {
            fac_rate.clear();
            loadingDialog.HideDialog();
            Toasty.error(this,"Facilities Rating Failed", Toast.LENGTH_SHORT,true).show();
        }
    }

    @Override
    public void onReviewSentStatus(boolean status) {
        if (status)
        {
            loadingDialog.HideDialog();
            Toast.makeText(this, "Review Submitted..", Toast.LENGTH_SHORT).show();
            tv_review.setText("");
        }
        else
        {
            loadingDialog.HideDialog();
            Toast.makeText(this, "Review Not Sent..", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.lplus.activities.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.lplus.R;
import com.lplus.activities.Adapters.CustomFavouriteListAdapter;
import com.lplus.activities.DBHelper.AddFavoutiteTable;
import com.lplus.activities.Interfaces.ListDataChangedInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.FavouriteObject;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity implements ListDataChangedInterface {

    ListView listView = null;
    TextView no_fav = null;
    CardView fav_show_all = null;
    CustomFavouriteListAdapter customFavouriteListAdapter = null;
    AddFavoutiteTable addFavoutiteTable = null;
    ArrayList<FavouriteObject> favouriteObjects = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_favourite);
        listView = findViewById(R.id.fav_list_view);
        no_fav = findViewById(R.id.no_fav_text);
        fav_show_all = findViewById(R.id.fav_show_all);
        Toolbar mToolbar =  findViewById(R.id.toolbar);
        mToolbar.setTitle("Favourites");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Your code
                finish();
            }
        });
        final ArrayList<String> place_id = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> address = new ArrayList<>();
        addFavoutiteTable = new AddFavoutiteTable(this);

        //extract data from database
        favouriteObjects = addFavoutiteTable.ReadRecords();
        addFavoutiteTable.CloseConnection();
        for(FavouriteObject fav: favouriteObjects) {
            place_id.add(fav.getFavourite_place_id());
            names.add(fav.getFavourite_name());
            address.add(fav.getFavourite_address());
        }
        customFavouriteListAdapter = new CustomFavouriteListAdapter(this,place_id,names,address);
        customFavouriteListAdapter.setListener(this);
        listView.setAdapter(customFavouriteListAdapter);
        if(customFavouriteListAdapter.getCount() != 0)
        {
            fav_show_all.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            no_fav.setVisibility(View.GONE);
        }
        else {
            fav_show_all.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            no_fav.setVisibility(View.VISIBLE);
        }
        fav_show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation myAnim = AnimationUtils.loadAnimation(FavouriteActivity.this, R.anim.bounce);
                fav_show_all.startAnimation(myAnim);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra(Keys.FAVORITES_ARRAY_LIST, place_id);
                        setResult(1,intent);
                        finish();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    @Override
    public void onDataChanged() {
        customFavouriteListAdapter.notifyDataSetChanged();
        if(customFavouriteListAdapter.getCount() == 0)
        {
            fav_show_all.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            no_fav.setVisibility(View.VISIBLE);
        }
        else{
            fav_show_all.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            no_fav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClicked(FavouriteObject selectedObject)
    {
        Intent intent = new Intent();
        intent.putExtra(Keys.MARKER_LATITUDE, selectedObject.getFavourite_lat());
        intent.putExtra(Keys.MARKER_LONGITUDE, selectedObject.getFavourite_lng());
        setResult(2,intent);
        finish();
    }
}

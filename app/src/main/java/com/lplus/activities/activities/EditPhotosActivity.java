package com.lplus.activities.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.lplus.R;
import com.lplus.activities.Adapters.CustomDeletePhotosAdapter;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.ListDataChangedInterface;
import com.lplus.activities.JavaFiles.PhotoStoreInfo;
import com.lplus.activities.Macros.Keys;

import java.util.ArrayList;
import java.util.Arrays;

public class EditPhotosActivity extends AppCompatActivity implements ListDataChangedInterface{
    RecyclerView delete_listView = null;
    CardView delete_photo_save = null;
    TextView no_photo_selected = null;
    CustomDeletePhotosAdapter customDeletePhotosAdapter = null;
    PhotoStoreInfo photoStoreInfo = null;
    ArrayList<String> photos = null;
    ArrayList<String> photos_uuid = null;
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photos);
        tinyDB = new TinyDB(EditPhotosActivity.this);
        photos = tinyDB.getListString(Keys.TINYDB_PHOTO_LIST);
        photos_uuid = tinyDB.getListString(Keys.TINYDB_PHOTO_UUID_LIST);
        delete_listView = findViewById(R.id.selected_photo_list);
        delete_photo_save = findViewById(R.id.delete_photo_save);
        no_photo_selected = findViewById(R.id.no_photo_selected);
        Toolbar mToolbar =  findViewById(R.id.toolbar);
        mToolbar.setTitle("Selected Photos");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Your code
                finish();
            }
        });

        //fetch photos from sharedpreference and store it in photos

        photoStoreInfo = new PhotoStoreInfo();
        photoStoreInfo.setPhoto_array(photos);
        photoStoreInfo.setPhoto_uuid_array(photos_uuid);
        customDeletePhotosAdapter = new CustomDeletePhotosAdapter(this,photoStoreInfo);
        customDeletePhotosAdapter.setRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        delete_listView.setLayoutManager(linearLayoutManager);
        delete_listView.setAdapter(customDeletePhotosAdapter);


        if(photoStoreInfo.getPhoto_array().size() != 0)
        {
            delete_listView.setVisibility(View.VISIBLE);
            no_photo_selected.setVisibility(View.GONE);
        }
        else {
            delete_listView.setVisibility(View.GONE);
            no_photo_selected.setVisibility(View.VISIBLE);
        }
        delete_photo_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation myAnim = AnimationUtils.loadAnimation(EditPhotosActivity.this, R.anim.bounce);
                delete_photo_save.startAnimation(myAnim);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
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
        customDeletePhotosAdapter.notifyDataSetChanged();
        if(tinyDB.getListString(Keys.TINYDB_PHOTO_LIST).size() == 0)
        {
            delete_listView.setVisibility(View.GONE);
            no_photo_selected.setVisibility(View.VISIBLE);
        }
        else{
            delete_listView.setVisibility(View.VISIBLE);
            no_photo_selected.setVisibility(View.GONE);
        }
    }
}

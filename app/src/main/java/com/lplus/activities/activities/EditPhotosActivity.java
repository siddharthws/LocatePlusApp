package com.lplus.activities.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.lplus.R;
import com.lplus.activities.Adapters.CustomDeletePhotosAdapter;
import com.lplus.activities.Adapters.CustomFavouriteListAdapter;
import com.lplus.activities.Interfaces.ListDataChangedInterface;
import com.lplus.activities.JavaFiles.PhotoStoreInfo;

import java.util.ArrayList;

public class EditPhotosActivity extends AppCompatActivity implements ListDataChangedInterface{
    ListView delete_listView = null;
    CardView delete_photo_save = null;
    TextView no_photo_selected = null;
    CustomDeletePhotosAdapter customDeletePhotosAdapter = null;
    PhotoStoreInfo photoStoreInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photos);

        delete_listView = findViewById(R.id.fav_list_view);
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
        ArrayList<String> photos = new ArrayList<>();
        photoStoreInfo = new PhotoStoreInfo(photos);

        customDeletePhotosAdapter = new CustomDeletePhotosAdapter(this,photoStoreInfo);
        customDeletePhotosAdapter.setRefreshListener(this);
        delete_listView.setAdapter(customDeletePhotosAdapter);
        if(customDeletePhotosAdapter.getCount() != 0)
        {
            delete_photo_save.setVisibility(View.VISIBLE);
            delete_listView.setVisibility(View.VISIBLE);
            no_photo_selected.setVisibility(View.GONE);
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
        if(customDeletePhotosAdapter.getCount() == 0)
        {
            delete_photo_save.setVisibility(View.GONE);
            delete_listView.setVisibility(View.GONE);
            no_photo_selected.setVisibility(View.VISIBLE);
        }
        else{
            delete_photo_save.setVisibility(View.VISIBLE);
            delete_listView.setVisibility(View.VISIBLE);
            no_photo_selected.setVisibility(View.GONE);
        }
    }
}

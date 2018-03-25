package com.lplus.activities.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.lplus.R;

/**
 * Created by Sai_Kameswari on 25-03-2018.
 */

public abstract class BaseMapActivity extends AppCompatActivity implements OnMapReadyCallback {

        private GoogleMap mMap;

        protected int getLayoutId() {
            return R.layout.menu_drawer;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(getLayoutId());
            setUpMap();
        }

        @Override
        protected void onResume() {
            super.onResume();
            setUpMap();
        }

        @Override
        public void onMapReady(GoogleMap map) {
            if (mMap != null) {
                return;
            }
            mMap = map;
            startDemo();
        }

        private void setUpMap() {
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        }

        /**
         * Run the demo-specific code.
         */
        protected abstract void startDemo();

        protected GoogleMap getMap() {
            return mMap;
        }

}


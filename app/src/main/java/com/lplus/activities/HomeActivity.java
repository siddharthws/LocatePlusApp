package com.lplus.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveCanceledListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;
import com.lplus.R;
import com.lplus.activities.Dialogs.AddPlaceDialog;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Interfaces.AddPlaceInterface;

import org.json.JSONException;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity implements  OnMapReadyCallback,
                                                                OnMapClickListener,
                                                                OnNavigationItemSelectedListener,
                                                                OnCameraMoveListener,
                                                                OnCameraIdleListener,
                                                                OnCameraMoveCanceledListener,
                                                                ConnectionCallbacks,
                                                                OnConnectionFailedListener,
                                                                 AddPlaceInterface{

    private GoogleMap mMap;
    private final int REQUEST_PERMISSION = 1;
    private static LatLng currentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private View mapView;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private LinearLayout ll_map;
    private ImageButton zoomlevel;
    private LoadingDialog loadingDialog;
    private static AddPlaceDialog addPlaceDialog;

    public HomeActivity() {
    }

    //-----------------------OVERRIDES------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_drawer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ScrollView parentScroll=findViewById(R.id.parent_scroll_desc);
        ScrollView childScroll=findViewById(R.id.child_scroll_facility);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(1);
            }
        });
        navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_white_24dp));
        navigationView.setNavigationItemSelectedListener(this);

        //get Linear Layout
        ll_map = findViewById(R.id.ll_map);
        zoomlevel = findViewById(R.id.zoomlevel);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        //touch listeners for scroll

       /* parentScroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.child_scroll_facility).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        childScroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event)
            {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/

        //check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION);
            return;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //if permissions given display map
        displayMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    displayMap();
                } else {
                    System.exit(1);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
    }

    @Override
    public void onCameraMove() {
        getSupportActionBar().hide();
        ll_map.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCameraIdle() {
        getSupportActionBar().show();
        ll_map.setVisibility(View.VISIBLE);
        float zoomLevel = mMap.getCameraPosition().zoom;
        if(zoomLevel > 7.2)
        {
            zoomlevel.setVisibility(View.VISIBLE);
        }
        if(zoomLevel <= 7.2)
        {
            zoomlevel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCameraMoveCanceled() {
        getSupportActionBar().show();
        ll_map.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.favorites: {

                break;
            }
            case R.id.help: {

                break;
            }
            case R.id.rate_us: {

                break;
            }
            case R.id.exit: {
                System.exit(1);
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //-----------------------PRIVATE API'S-------------------------//
    private void displayMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //set UI Settings
        setUISettings();

        //set my location enabled
        mMap.setOnMapClickListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setOnCameraMoveListener(this);

        recenterMap();
        try {
            GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.maharashtra_districts, getApplicationContext());

            GeoJsonPolygonStyle style = layer.getDefaultPolygonStyle();
            style.setFillColor(Color.TRANSPARENT);
            style.setStrokeColor(Color.RED);
            style.setStrokeWidth(1F);

            layer.addLayerToMap();

        } catch (IOException ex) {
            Log.e("IOException", ex.getLocalizedMessage());
        } catch (JSONException ex) {
            Log.e("JSONException", ex.getLocalizedMessage());
        }
    }

    private void setUISettings() {
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setCompassEnabled(true);

        // change compass position
        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the view
            View locationCompass = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("5"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationCompass.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 220, 30, 0);
        }
    }

    private void checkForGpsOn() {
        LocationRequest mLocationRequest;
        GoogleApiClient mGoogleApiClient;
        PendingResult<LocationSettingsResult> result;
        final int REQUEST_LOCATION = 199;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        {
                            getCurrentLocation();
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    }
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    HomeActivity.this,
                                    REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        System.out.println("mfuesdClient: "+mFusedLocationClient.toString());
        if (mFusedLocationClient != null) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        System.out.println("location: "+location.getLatitude());
                        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        animateCamera();
                    }
                }
            });
        }
    }

    private void animateCamera()
    {
        if(currentLocation !=null) {
            loadingDialog.HideDialog();
           // mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));//Moves the camera to users current longitude and latitude
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
        }
    }

    private void recenterMap()
    {
        //move camera to Maharastra state
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.65222, 75.82802), 6.0f));
    }

    //=======================PUBLIC API'S=====================//
    public void zoomOutClick(View view)
    {
        zoomlevel.setVisibility(View.GONE);
        recenterMap();
    }

    public void currentLocationClick(View view)
    {
        //show dialog
        loadingDialog = new LoadingDialog(this, "Fetching Current Location..");
        loadingDialog.ShowDialog();
        checkForGpsOn();
    }

    public void onFilterClick(View view)
    {
        Toast.makeText(this, "Clicked on filter",Toast.LENGTH_SHORT).show();
    }

    public void onAddPlaceClick(View view)
    {
        //check for internet connection
        if(!InternetConnectivityCheck.isConnectedToNetwork(HomeActivity.this))
        {
            Toast.makeText(this, "Please connect to internet and try again...", Toast.LENGTH_SHORT).show();
            return;
        }
        final LoadingDialog loadingDialog = new LoadingDialog(this, "Fetching Coordinates..");
        loadingDialog.ShowDialog();

        LatLng center = mMap.getCameraPosition().target;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.HideDialog();
            }
        }, 2000);

        Toast.makeText(this, "Clicked Latitude: "+center.latitude+" Longitude: "+center.longitude,Toast.LENGTH_SHORT).show();
        center = null;

        addPlaceDialog = new AddPlaceDialog(HomeActivity.this);
        addPlaceDialog.SetListener(this);
        addPlaceDialog.ShowDialog();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    public void onSaveClick() {
        Toast.makeText(this, "Save Clicked", Toast.LENGTH_SHORT).show();
        addPlaceDialog.HideDialog();
    }

    @Override
    public void onCancelClick() {
        Toast.makeText(this, "Cancel Clicked", Toast.LENGTH_SHORT).show();
        addPlaceDialog.HideDialog();
    }
}

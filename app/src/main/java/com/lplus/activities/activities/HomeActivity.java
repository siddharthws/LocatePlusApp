package com.lplus.activities.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveCanceledListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.lplus.R;
import com.lplus.activities.DBHelper.AddCategoryTable;
import com.lplus.activities.Dialogs.FilterDialog;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Extras.CheckGPSOn;
import com.lplus.activities.Extras.InternetConnectivityCheck;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.CategorySelectedInterface;
import com.lplus.activities.JavaFiles.Geocoding;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.MarkerObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements  OnMapReadyCallback,
                                                                OnMapClickListener,
                                                                OnNavigationItemSelectedListener,
                                                                OnCameraMoveListener,
                                                                OnCameraIdleListener,
                                                                OnCameraMoveCanceledListener,
                                                                CategorySelectedInterface,
                                                                CheckGPSOn.CheckGPSInterface, Geocoding.geocodingInterface {

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
    private FilterDialog filterDialog;
    private SupportMapFragment mapFragment;
    private SharedPreferences app_sharePref;
    private static LatLng center;
    private TinyDB tinyDB;

    final int REQUEST_LOCATION = 199;
    /*CameraPhoto cameraPhoto;

    final int CAMERA_REQUEST = 13323;

    ImageView showPhoto = null,addphoto = null;
    Dialog dialog = null;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_drawer);

        app_sharePref = getSharedPreferences(Keys.SHARED_PREF_NAME, MODE_PRIVATE);
        tinyDB = new TinyDB(HomeActivity.this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ScrollView parentScroll=findViewById(R.id.parent_scroll_desc);
        ScrollView childScroll=findViewById(R.id.child_scroll_facility);

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
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

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
                }
                return;
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {}

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
                startActivity(new Intent(HomeActivity.this,FavouriteActivity.class));
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

    private void displayMap() {
        //set UI Settings
        setUISettings();

        //set my location enabled
        mMap.setOnMapClickListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setOnCameraMoveListener(this);

        recenterMap();
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

    private void checkForGpsOn()
    {
        CheckGPSOn checkGPSOn = new CheckGPSOn(this);
        checkGPSOn.setListener(this);
        checkGPSOn.Init();
    }
    @Override
    public void GPSResult(int statusCode, Status resultStatus)
    {
        switch (statusCode)
        {
            case 1: {getCurrentLocation(); break;}
            case 2:
            {
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    resultStatus.startResolutionForResult(
                            HomeActivity.this,
                            REQUEST_LOCATION);
                } catch (IntentSender.SendIntentException e) {
                    // Ignore the error.
                }
                break;
            }
            case 3: break;
        }
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

        setAllMarkers();
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
        AddCategoryTable addCategoryTable = new AddCategoryTable(this);
        ArrayList<ArrayList> categoriesArrayList = addCategoryTable.ReadRecords();
        addCategoryTable.CloseConnection();

        ArrayList<String> categoriesList = categoriesArrayList.get(1);

        System.out.println("list at home: "+categoriesList.toString());

        filterDialog = new FilterDialog(this, categoriesList);
        filterDialog.setListener(this);
        filterDialog.ShowDialog();
    }

    public void onAddPlaceClick(View view)
    {
        //check for internet connection
        if(!InternetConnectivityCheck.isConnectedToNetwork(HomeActivity.this))
        {
            Toast.makeText(this, "Please connect to internet and try again...", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingDialog = new LoadingDialog(this, "Fetching Coordinates..");
        loadingDialog.ShowDialog();

        center = mMap.getCameraPosition().target;

        //get address using geocoding
        Geocoding geocoding = new Geocoding(HomeActivity.this, center.latitude, center.longitude);
        geocoding.setListener(this);
        geocoding.execute();
        center = null;
    }
    @Override
    public void onAddressFetched(String result, double latit, double longi) {

        loadingDialog.HideDialog();
        Toast.makeText(this, "Clicked Latitude: "+latit+" Longitude: "+longi,Toast.LENGTH_SHORT).show();

        System.out.println("Addrs fetched: "+result);
        System.out.println("Latit fetched: "+latit);
        System.out.println("Longi fetched: "+longi);

        Intent addPlaceIntent = new Intent(HomeActivity.this, AddPlaceActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putDouble(Keys.CENTER_LATITUDE, latit);
        mBundle.putDouble(Keys.CENTER_LONGITUDE, longi);
        mBundle.putString(Keys.CENTER_ADDRESS, result);
        addPlaceIntent.putExtras(mBundle);
        startActivity(addPlaceIntent);
    }

    @Override
    public void onAddressFetchFailed()
    {
        loadingDialog.HideDialog();
        Toast.makeText(this, "Sorry...Cannot Fetch the address...Try again",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onApplyClick(List<String> selectedcategories)
    {
        Toast.makeText(this, "selected: "+selectedcategories.toString(), Toast.LENGTH_SHORT).show();
        filterDialog.HideDialog();

        //LoadingDialog loadingDialog = new LoadingDialog(this, "Applying Filters......");
        //loadingDialog.ShowDialog();
    }

    @Override
    public void onCancelClick(List<String> selectedcategories)
    {
        if(selectedcategories!=null)
        {
            filterDialog.HideDialog();
            Toast.makeText(this, "selected: "+selectedcategories.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        mapFragment.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapFragment.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        System.gc();
        super.onLowMemory();
    }
    public void setAllMarkers()
    {
        ArrayList<MarkerObject> markersList = tinyDB.getListObject(Keys.TINYDB_MARKERS, MarkerObject.class);
        LatLng position;
        for(MarkerObject markerObject : markersList)
        {
            position = new LatLng(markerObject.getMarkerLatitude(), markerObject.getMarkerLongitude());
            mMap.addMarker(new MarkerOptions().position(position).title(markerObject.getMarkerName()));
        }
    }
}

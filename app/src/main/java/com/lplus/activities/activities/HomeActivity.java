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
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.ClusterManager;
import com.lplus.R;
import com.lplus.activities.DBHelper.AddCategoryTable;
import com.lplus.activities.DBHelper.AddFavoutiteTable;
import com.lplus.activities.DBHelper.MarkersTable;
import com.lplus.activities.Dialogs.FilterDialog;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Dialogs.MarkerSummaryDialog;
import com.lplus.activities.Extras.CacheData;
import com.lplus.activities.Extras.CheckGPSOn;
import com.lplus.activities.Extras.InternetConnectivityCheck;
import com.lplus.activities.Extras.ServerParseStatics;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.CategorySelectedInterface;
import com.lplus.activities.Interfaces.GetMarkerInteface;
import com.lplus.activities.JavaFiles.Geocoding;
import com.lplus.activities.JavaFiles.MyItem;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.FavouriteObject;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Server.GetMarkersServerClass;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements  OnMapReadyCallback,
                                                                OnMapClickListener,
                                                                OnNavigationItemSelectedListener,
                                                                OnCameraMoveListener,
                                                                OnCameraIdleListener,
                                                                OnCameraMoveCanceledListener,
                                                                CategorySelectedInterface,
                                                                CheckGPSOn.CheckGPSInterface,
                                                                Geocoding.geocodingInterface,
                                                                GoogleMap.OnMarkerClickListener, GetMarkerInteface {

    private GoogleMap mMap;
    private ClusterManager<MyItem> mClusterManager;
    private final int REQUEST_PERMISSION = 1;
    private static LatLng currentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private View mapView;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private LinearLayout ll_map;
    private ImageButton zoomlevel, clearFavorites, ibComplaint;
    private LoadingDialog loadingDialog;
    private FilterDialog filterDialog;
    private SupportMapFragment mapFragment;
    private SharedPreferences app_sharePref;
    private static LatLng center;
    private TinyDB tinyDB;

    final int REQUEST_LOCATION = 199;
   /* CameraPhoto cameraPhoto;

    final int CAMERA_REQUEST = 13323;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_drawer);

        app_sharePref = getSharedPreferences(Keys.SHARED_PREF_NAME, MODE_PRIVATE);
        tinyDB = new TinyDB(HomeActivity.this);
        tinyDB.putListString(Keys.TINYDB_PHOTO_LIST,new ArrayList<String>());
        tinyDB.putListString(Keys.TINYDB_PHOTO_UUID_LIST,new ArrayList<String>());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*ScrollView parentScroll=findViewById(R.id.parent_scroll_desc);
        ScrollView childScroll=findViewById(R.id.child_scroll_facility);*/

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
        clearFavorites = findViewById(R.id.favorite_clear);
        clearFavorites.setVisibility(View.GONE);

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

    private void setUpClusterer() {

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }
    private void addItems() {
        ArrayList<MarkerObject> markersList = CacheData.cacheMarkers;
        System.out.println("Total markers: "+ markersList.size());

        // Add ten cluster items in close proximity, for purposes of this example.

        for(MarkerObject item : markersList)
        {
            MyItem offsetItem = new MyItem(item.getMarkerLatitude(), item.getMarkerLongitude());
            mClusterManager.addItem(offsetItem);
        }
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

        System.out.println("Zoom Level: "+mMap.getCameraPosition().zoom);
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
        float zoomLevel = mMap.getCameraPosition().zoom;
        if(zoomLevel > 7.2)
        {
            setAllMarkers();
        }
        if(zoomLevel <= 7.2)
        {
            if(mMap != null)
            {
                mMap.clear();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {

            case R.id.sync_places: {
                    System.out.println("Syncing Map......");
                    ClearMap();

                    // add markers from database to the map
                    GetMarkersServerClass getMarkersServerClass = new GetMarkersServerClass(this);
                    getMarkersServerClass.SetListener(this);
                    getMarkersServerClass.execute();

                break;
            }

            case R.id.favorites: {
                startActivityForResult((new Intent(HomeActivity.this,FavouriteActivity.class)), 1);
                break;
            }
            case R.id.settings:
            {
                startActivity(new Intent(HomeActivity.this,SOSContactsActivity.class));
                break;
            }
            case R.id.help: {
                tinyDB.putBoolean(Keys.HELP_SLIDER, true);
                startActivity(new Intent(HomeActivity.this,HelpSliderActivity.class));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(tinyDB == null)
        {
            tinyDB = TinyDB.Init(this);
        }
        long contact1 = tinyDB.getLong(Keys.CONTACT_1, -1);
        long contact2 = tinyDB.getLong(Keys.CONTACT_2, -1);
        long contact3 = tinyDB.getLong(Keys.CONTACT_3, -1);

        //check for numbers
        if(contact1 == -1 || contact2 == -1 || contact3 == -1)
        {
            //Redirect to save contacts
            Intent save=new Intent(HomeActivity.this,SOSContactsActivity.class);
            startActivity(save);
        }
        else
        {
            Intent sos=new Intent(HomeActivity.this,SmsActivity.class);
            startActivity(sos);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer != null)
        {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    private void displayMap() {

        //setup Cluster
        //setUpClusterer();
        //set UI Settings
        setUISettings();

        //set my location enabled
        mMap.setOnMapClickListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnMarkerClickListener(this);

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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.65222, 75.82802), 7.2f));

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
        //clear any filters applied
        clearFavorites.setVisibility(View.GONE);
        setAllMarkers();

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
            Snackbar snackbar = Snackbar.make(view,"Please connect to internet and try again...", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                }
            });
        }
        loadingDialog = new LoadingDialog(this, "Fetching Coordinates");
        loadingDialog.ShowDialog();

        center = mMap.getCameraPosition().target;

        //get address using geocoding
        Geocoding geocoding = new Geocoding(HomeActivity.this, center.latitude, center.longitude);
        geocoding.setListener(this);
        loadingDialog.HideDialog();
        geocoding.execute();
        center = null;
    }

    public void onAddComplaint(View view)
    {
        //check for internet connection
        if(!InternetConnectivityCheck.isConnectedToNetwork(HomeActivity.this))
        {
            Snackbar snackbar = Snackbar.make(view,"Please connect to internet and try again...", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                }
            });
        }
        loadingDialog = new LoadingDialog(this, "Fetching Coordinates");
        loadingDialog.ShowDialog();

        center = mMap.getCameraPosition().target;

        //get address using geocoding
        Geocoding geocoding = new Geocoding(HomeActivity.this, center.latitude, center.longitude);
        geocoding.setListener(new Geocoding.geocodingInterface() {
            @Override
            public void onAddressFetched(String result, double latitude, double longitude) {

                Intent addComplaintIntent = new Intent(HomeActivity.this, AddComplaintActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putDouble(Keys.CENTER_LATITUDE, latitude);
                mBundle.putDouble(Keys.CENTER_LONGITUDE, longitude);
                mBundle.putString(Keys.CENTER_ADDRESS, result);
                addComplaintIntent.putExtras(mBundle);
                startActivityForResult(addComplaintIntent, 2);
            }

            @Override
            public void onAddressFetchFailed(int status) {
                if(status == -1)
                {
                    Toast.makeText(HomeActivity.this, "Sorry...Cannot Fetch the address...Try again",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(HomeActivity.this, "Cannot add marker outside Maharashtra....",Toast.LENGTH_SHORT).show();
                }

            }
        });
        loadingDialog.HideDialog();
        geocoding.execute();
        center = null;
    }
    @Override
    public void onAddressFetched(String result, double latit, double longi) {


        Intent addPlaceIntent = new Intent(HomeActivity.this, AddPlaceActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putDouble(Keys.CENTER_LATITUDE, latit);
        mBundle.putDouble(Keys.CENTER_LONGITUDE, longi);
        mBundle.putString(Keys.CENTER_ADDRESS, result);
        addPlaceIntent.putExtras(mBundle);
        startActivityForResult(addPlaceIntent, 2);

    }

    @Override
    public void onAddressFetchFailed(int status)
    {
        if(status == -1)
        {
            Toast.makeText(this, "Sorry...Cannot Fetch the address...Try again",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Cannot add marker outside Maharashtra....",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        tinyDB.putListString(Keys.TINYDB_PHOTO_LIST,new ArrayList<String>());
        tinyDB.putListString(Keys.TINYDB_PHOTO_UUID_LIST,new ArrayList<String>());
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
        AddFavoutiteTable addFavoutiteTable;
        if(CacheData.cacheMarkers == null)
        {
            MarkersTable markersTable = new MarkersTable(this);
            CacheData.cacheMarkers = markersTable.ReadRecords();
            markersTable.CloseConnection();
        }
        addFavoutiteTable = new AddFavoutiteTable(this);
        ArrayList<MarkerObject> markersList = CacheData.cacheMarkers;
        LatLng position;
        for(MarkerObject markerObject : markersList)
        {
            position = new LatLng(markerObject.getMarkerLatitude(), markerObject.getMarkerLongitude());
            if(addFavoutiteTable.isFavourite(markerObject.getMarkerID())) {

                Marker marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholderfav)).position(position).title(markerObject.getMarkerName())
                        .snippet(markerObject.getMarkerCategory()));
                marker.setTag(markerObject);
            }
            else {
                Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(markerObject.getMarkerName())
                        .snippet(markerObject.getMarkerCategory()));
                marker.setTag(markerObject);
            }
        }
    }

    public void setFavMarkers()
    {
        clearFavorites.setVisibility(View.VISIBLE);
        if(mMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mMap.clear();
        }
        LatLng position;
        ArrayList<MarkerObject> selectedMarkers = CacheData.cacheFavoriteMarkers;
        System.out.println("Fav size: "+selectedMarkers.size());
        for(MarkerObject markerObject : selectedMarkers)
        {
            position = new LatLng(markerObject.getMarkerLatitude(), markerObject.getMarkerLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(markerObject.getMarkerName())
                            .snippet(markerObject.getMarkerCategory()));
            marker.setTag(markerObject);
        }
        //move camera to Maharastra state
        mMap.
                animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.65222, 75.82802), 8.0f));
    }

    public void setFilteredeMarkers(ArrayList<MarkerObject> filteredMarkers)
    {
        clearFavorites.setImageResource(R.drawable.ic_clear_all_white_24dp);
        clearFavorites.setVisibility(View.VISIBLE);
        if(mMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mMap.clear();
        }
        LatLng position;
        for(MarkerObject markerObject : filteredMarkers)
        {
            position = new LatLng(markerObject.getMarkerLatitude(), markerObject.getMarkerLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(markerObject.getMarkerName())
                    .snippet(markerObject.getMarkerCategory()));
            marker.setTag(markerObject);
        }
        //move camera to Maharastra state
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(filteredMarkers.get(0).getMarkerLatitude(), filteredMarkers.get(0).getMarkerLongitude()), 8.0f));
    }

    public void setFavMarker(LatLng selectedLatLng)
    {
        setAllMarkers();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 16));
    }

    @Override
    public boolean onMarkerClick(final Marker marker)
    {
        final MarkerObject selectedMarker = (MarkerObject) marker.getTag();

        MarkerSummaryDialog markerSummaryDialog = new MarkerSummaryDialog(HomeActivity.this, selectedMarker);
        markerSummaryDialog.ShowDialog();
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        switch(requestCode) {
            case 1: {
                if (resultCode == 2) {
                    double lat = data.getDoubleExtra(Keys.MARKER_LATITUDE, 0.0);
                    double lng = data.getDoubleExtra(Keys.MARKER_LONGITUDE, 0.0);
                    LatLng selectedLatLng = new LatLng(lat, lng);
                    setFavMarker(selectedLatLng);
                }
                if (resultCode == 1) {
                    //init cache
                    if (CacheData.cacheFavoriteMarkers == null) {
                        CacheData.cacheFavoriteMarkers = new ArrayList<>();
                    }
                    //set all markers
                    AddFavoutiteTable addFavoutiteTable = new AddFavoutiteTable(this);
                    MarkersTable markersTable = new MarkersTable(this);
                    ArrayList<FavouriteObject> favouriteObjects = addFavoutiteTable.ReadRecords();
                    ArrayList<MarkerObject> markersList = new ArrayList<>();
                    MarkerObject markerObject = null;
                    for (FavouriteObject favouriteObject : favouriteObjects) {
                        markerObject = markersTable.getObject(favouriteObject.getFavourite_place_id());
                        markersList.add(markerObject);
                        CacheData.cacheFavoriteMarkers.add(markerObject);
                    }
                    markersTable.CloseConnection();
                    addFavoutiteTable.CloseConnection();
                    //cal the method
                    setFavMarkers();
                }
                if (resultCode == 3) {
                    setAllMarkers();
                }
                break;
            }
            case 2:
            {
                setAllMarkers();
                break;
            }
        }
    }

    public void clearFavClick(View view)
    {
        clearFavorites.setVisibility(View.GONE);
        ClearMap();
        setAllMarkers();
    }

    private void ClearMap()
    {
        if(mMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mMap.clear();
        }
    }

    public GoogleMap getMap()
    {
        if(mMap != null)
        {
            return mMap;
        }
        return null;
    }

    @Override
    public void onMarkerFetchStatus(boolean status) {
        if (status)
        {
            setAllMarkers();
        }
        else
        {
            Toast.makeText(this, "Failed to Sync Markers...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onApplyClick(boolean status, List<String> selectedcategories)
    {
        if (status)
        {
            filterDialog.HideDialog();
            loadingDialog = new LoadingDialog(this, "Applying Filters...");
            loadingDialog.ShowDialog();

            //fetch All Records
            ClearMap();
            ArrayList<MarkerObject> filteredMarkers = ServerParseStatics.filteredMarkers(selectedcategories, this);
            tinyDB = new TinyDB(this);
            tinyDB.putListString("selectedCategories", (ArrayList<String>) selectedcategories);
            if(filteredMarkers.size() > 0)
            {
                loadingDialog.HideDialog();
                setFilteredeMarkers(filteredMarkers);
            }
            else
            {
                loadingDialog.HideDialog();
                Toast.makeText(this, "No Markers Existing", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if(selectedcategories!=null)
            {
                filterDialog.HideDialog();
            }
        }
    }
}
